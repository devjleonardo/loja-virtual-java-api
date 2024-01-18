package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joseleonardo.lojavirtual.api.dto.venda.VendaCobrancaDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.constants.AsaasPagamentoConstants;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.AsaasPagamentoErrorListResponseDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.cobrancas.criar_cobranca_carta_credito.AsaasPagamentoCartaoCreditoDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.cobrancas.criar_cobranca_carta_credito.AsaasPagamentoCobrancaGeradaCartaoCreditoDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.cobrancas.criar_cobranca_carta_credito.AsaasPagamentoCriarCobrancaCartaoCreditoDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.cobrancas.criar_cobranca_carta_credito.AsaasPagamentoTitularCartaoCreditoDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.service.AsaasPagamentoService;
import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.AsaasBoletoPix;
import com.joseleonardo.lojavirtual.model.AsaasStatusPagamentoCartaoCredito;
import com.joseleonardo.lojavirtual.model.PessoaFisica;
import com.joseleonardo.lojavirtual.model.VendaCompraLojaVirtual;
import com.joseleonardo.lojavirtual.repository.AsaasBoletoPixRepository;
import com.joseleonardo.lojavirtual.repository.AsaasStatusPagamentoCartaoCreditoRepository;
import com.joseleonardo.lojavirtual.repository.VendaCompraLojaVirtualRepository;
import com.joseleonardo.lojavirtual.ssl.HostIgnoringSSLClient;
import com.joseleonardo.lojavirtual.util.ValidacaoCPF;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@RestController
public class AsaasPagamentoController {
	
	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	@Autowired
	private AsaasBoletoPixRepository asaasBoletoPixRepository;
	
	@Autowired
	private AsaasStatusPagamentoCartaoCreditoRepository asaasStatusPagamentoCartaoCreditoRepository;
	
	@Autowired
	private AsaasPagamentoService asaasPagamentoService;
	
	@ResponseBody
	@PostMapping(value = "**/asaasFinalizarCompraCartao")
	public ResponseEntity<String> finalizarCompraCartaoAsaas(
			@RequestParam("cardNumber") String cardNumber,
			@RequestParam("holderName") String holderName,
			@RequestParam("securityCode") String securityCode,
			@RequestParam("expirationMonth") String expirationMonth,
			@RequestParam("expirationYear") String expirationYear,
			@RequestParam("idVenda") Long idVenda,
			@RequestParam("cpf") String cpf,
			@RequestParam("qtdparcela") Integer qtdparcela,
			@RequestParam("cep") String cep,
			@RequestParam("rua") String rua,
			@RequestParam("numero") String numero,
			@RequestParam("estado") String estado,
			@RequestParam("cidade") String cidade) throws Exception {
		// Busca a venda com base no ID fornecido como parâmetro
	    VendaCompraLojaVirtual vendaCompraLojaVirtual =
	        vendaCompraLojaVirtualRepository.findById(idVenda).orElse(null);

	    // Verificando se a venda foi encontrada
	    if (vendaCompraLojaVirtual == null) {
	        throw new LojaVirtualException(
	            "Venda não encontrada para o ID da venda fornecido: " + idVenda
	        );
	    }
	    
	    // Limpa a formatação do CPF
	    String cpfLimpo =  cpf.replaceAll("\\.", "").replaceAll("\\-", "");
		
	    // Verifica se o CPF fornecido é válido
		if (!ValidacaoCPF.isCPF(cpfLimpo)) {
			return new ResponseEntity<String>("CPF informado é inválido.", HttpStatus.OK);
		}
		
		// Verifica se a quantidade de parcelas está dentro do intervalo permitido
		if (qtdparcela > 12 || qtdparcela <= 0) {
		    return new ResponseEntity<String>( 
		        "Quantidade de parcelar deve ser de  1 até 12.", HttpStatus.OK
		    );
		}
		
		// Verifica se o valor total da venda é maior que zero
		if (vendaCompraLojaVirtual.getValorTotal().doubleValue() <= 0) {
			return new ResponseEntity<String>("Valor da venda não pode ser Zero(0).", HttpStatus.OK);
		}
		
		// Busca boletos e pix não pagos associados à venda
		List<AsaasBoletoPix> cobrancas = asaasBoletoPixRepository.buscarBoletosNaoPagosPorIdVenda(idVenda);
		
		// Busca e exclui boletos e pix não pagos associados à venda
		for (AsaasBoletoPix asaasBoletoPix : cobrancas) {
			asaasBoletoPixRepository.deleteById(asaasBoletoPix.getId());
			asaasBoletoPixRepository.flush();
		}
		
		/* *** INICIO - Gerando cobrança por cartão de crédito *** */
		
		// Inicializa um objeto DTO para informações de cobrança vinculado à venda
		VendaCobrancaDTO vendaCobrancaDTO = new VendaCobrancaDTO();
		vendaCobrancaDTO.setCpfCnpjCliente(cpfLimpo);
		vendaCobrancaDTO.setNomeCliente(holderName);
		vendaCobrancaDTO.setTelefoneCliente(vendaCompraLojaVirtual.getPessoa().getTelefone());
		
		// Cria DTO para gerar cobrança por cartão de crédito
		AsaasPagamentoCriarCobrancaCartaoCreditoDTO criarCobrancaCartaoCreditoDTO = new AsaasPagamentoCriarCobrancaCartaoCreditoDTO();
		
		criarCobrancaCartaoCreditoDTO.setCustomer(
		    asaasPagamentoService.buscarOuCriarNovoCliente(vendaCobrancaDTO)
		);
		
		criarCobrancaCartaoCreditoDTO.setBillingType(AsaasPagamentoConstants.ASAAS_CREDIT_CARD);
		
		criarCobrancaCartaoCreditoDTO.setDescription(
		    "Venda realizada para cliente por cartão de crédito. Códido da venda: -> " + idVenda
		);
		
		// Define o valor da cobrança com base na quantidade de parcelas
		if (qtdparcela == 1) {
			criarCobrancaCartaoCreditoDTO.setValue(vendaCompraLojaVirtual.getValorTotal().floatValue());
			criarCobrancaCartaoCreditoDTO.setInstallmentValue(vendaCompraLojaVirtual.getValorTotal().floatValue());
		} else {
			BigDecimal valorParcela = vendaCompraLojaVirtual.getValorTotal()
			    .divide(BigDecimal.valueOf(qtdparcela), RoundingMode.DOWN)
			    .setScale(2, RoundingMode.DOWN);
			
			criarCobrancaCartaoCreditoDTO.setValue(valorParcela.floatValue());
			criarCobrancaCartaoCreditoDTO.setInstallmentValue(valorParcela.floatValue());
		}
		
		criarCobrancaCartaoCreditoDTO.setInstallmentCount(qtdparcela);
		
		criarCobrancaCartaoCreditoDTO.setDueDate(
		    new SimpleDateFormat("yyyy-MM-dd").format(
		        Calendar.getInstance().getTime())
		);
		
		// Dados do cartão de crédito
		AsaasPagamentoCartaoCreditoDTO cartaoCreditoDTO = new AsaasPagamentoCartaoCreditoDTO();
		cartaoCreditoDTO.setHolderName(holderName);
		cartaoCreditoDTO.setNumber(cardNumber);
		cartaoCreditoDTO.setExpiryMonth(expirationMonth);
		cartaoCreditoDTO.setExpiryYear(expirationYear);
		cartaoCreditoDTO.setCcv(securityCode);
		
		criarCobrancaCartaoCreditoDTO.setCreditCard(cartaoCreditoDTO);
		
		// Dados do titular do cartão de crédito
		PessoaFisica pessoaFisica = vendaCompraLojaVirtual.getPessoa();
		AsaasPagamentoTitularCartaoCreditoDTO titularCartaoCreditoDTO = new AsaasPagamentoTitularCartaoCreditoDTO();
		titularCartaoCreditoDTO.setName(pessoaFisica.getNome());
		titularCartaoCreditoDTO.setEmail(pessoaFisica.getEmail());
		titularCartaoCreditoDTO.setCpfCnpj(pessoaFisica.getCpf());
		titularCartaoCreditoDTO.setPostalCode(cep);
		titularCartaoCreditoDTO.setAddressNumber(numero);
		titularCartaoCreditoDTO.setAddressComplement(null);
		titularCartaoCreditoDTO.setPhone(pessoaFisica.getTelefone());
		titularCartaoCreditoDTO.setMobilePhone(pessoaFisica.getTelefone());
		
		criarCobrancaCartaoCreditoDTO.setCreditCardHolderInfo(titularCartaoCreditoDTO);
		
		// Convertendo o DTO para JSON
		String json = new ObjectMapper().writeValueAsString(criarCobrancaCartaoCreditoDTO);
		
	    // Criando um cliente HTTP ignorando SSL
	    Client client = new HostIgnoringSSLClient(AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX)
	        .createUnsecuredHttpClient();
	    
	    // Definindo o recurso da API da Asaas para criar cobrança com cartão de crédito
	    WebResource webResource = client.resource(
	        AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX + "payments/"
	    );
	    
	    // Enviando uma requisição POST para a API Asaas
	    ClientResponse clientResponse = webResource
	        .accept("application/json;charset=UTF-8")
	        .header("Content-Type", "application/json")
	        .header("access_token", AsaasPagamentoConstants.ASAAS_ACCESS_TOKEN_SANDBOX)
	        .post(ClientResponse.class, json);
	    
	    // Obtendo a resposta da API
	    String respostaApi = clientResponse.getEntity(String.class);
	    
	    // Fechando a resposta do cliente da API Asaas
	    clientResponse.close();
	    
	    // Criando um objeto ObjectMapper para realizar a conversão entre JSON e objetos Java
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    // Habilita o comportamento para aceitar uma string vazia como um objeto nulo durante a desserialização.
	    objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
	    
	    // Desabilita a falha ao encontrar propriedades desconhecidas durante a desserialização.
	    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	    
	    // Verificando se a resposta da requisição à API Asaas não foi bem-sucedida (status diferente de 200 OK)
	    if (clientResponse.getStatus() != 200) {
			for (AsaasBoletoPix asaasBoletoPix : cobrancas) {
				if (asaasBoletoPixRepository.existsById(asaasBoletoPix.getId())) {
					asaasBoletoPixRepository.deleteById(asaasBoletoPix.getId());
					asaasBoletoPixRepository.flush();
				}
			}
			
		    // Convertendo a resposta de erro da API Asaas para um objeto DTO usando o ObjectMapper
			AsaasPagamentoErrorListResponseDTO errorListResponseDTO = objectMapper.readValue(
				respostaApi, new TypeReference<AsaasPagamentoErrorListResponseDTO>() {}
			);
			
		    // Retornando uma resposta com uma mensagem indicando o erro na cobrança
			return new ResponseEntity<String>(
			    "Erro ao efetuar cobrança: " + errorListResponseDTO.listarErrors(), HttpStatus.OK
			);
	    }
	    
	    // Convertendo a resposta da API Asaas para um objeto DTO representando a cobrança gerada por cartão de crédito
	    AsaasPagamentoCobrancaGeradaCartaoCreditoDTO cobrancaGeradaCartaoCreditoDTO = 
	        new ObjectMapper().readValue(
	            respostaApi, new TypeReference<AsaasPagamentoCobrancaGeradaCartaoCreditoDTO>() {}
	        );
	    
	    // Inicializando uma lista para armazenar objetos AsaasStatusPagamentoCartaoCredito
	    List<AsaasStatusPagamentoCartaoCredito> statusPagamentosCartaoCredito = new ArrayList<>();
	    
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date dataVencimento = simpleDateFormat.parse(criarCobrancaCartaoCreditoDTO.getDueDate());
	    Calendar calendar = Calendar.getInstance();
	    
	    // Iterando sobre as parcelas da cobrança do cartão de crédito
	    for(int i = 1; i <= qtdparcela; i++) {
	    	AsaasStatusPagamentoCartaoCredito statusPagamentoCartaoCredito = new AsaasStatusPagamentoCartaoCredito();
	    	statusPagamentoCartaoCredito.setIdCobrancaAsaas(cobrancaGeradaCartaoCreditoDTO.getId());
	    	statusPagamentoCartaoCredito.setUrlFatura(cobrancaGeradaCartaoCreditoDTO.getInvoiceUrl());
	    	statusPagamentoCartaoCredito.setPago(false);
	    	statusPagamentoCartaoCredito.setDataVencimento(simpleDateFormat.format(dataVencimento));
	    	
	    	calendar.setTime(dataVencimento);
	    	calendar.add(Calendar.MONTH, 1);
	    	
	    	dataVencimento = calendar.getTime();
	    	
	    	BigDecimal valorParcela = BigDecimal.valueOf(criarCobrancaCartaoCreditoDTO.getInstallmentValue());
    		statusPagamentoCartaoCredito.setValorParcela(valorParcela);
    		
    	    // Configurando o valor da parcela e o valor total da cobrança para a parcela atual
	    	if (qtdparcela == 1) {
	    		statusPagamentoCartaoCredito.setValorTotal(
	    		    BigDecimal.valueOf(criarCobrancaCartaoCreditoDTO.getValue())
	    		);
	    	} else {
	    		statusPagamentoCartaoCredito.setValorTotal(valorParcela);
	    	}
	    	
	    	statusPagamentoCartaoCredito.setNumeroParcela(i);
	    	statusPagamentoCartaoCredito.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
	    	statusPagamentoCartaoCredito.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
	    	
	    	statusPagamentosCartaoCredito.add(statusPagamentoCartaoCredito);
	    }
	    
	    // Salvando todos os status de pagamentos no banco de dados
	    asaasStatusPagamentoCartaoCreditoRepository.saveAllAndFlush(statusPagamentosCartaoCredito);
	    
	    // Verificando se o status da cobrança do cartão de crédito é "CONFIRMED"
	    if (cobrancaGeradaCartaoCreditoDTO.getStatus().equalsIgnoreCase("CONFIRMED")) {
	        // Iterando sobre os status de pagamentos para marcar cada parcela como paga
	    	for (AsaasStatusPagamentoCartaoCredito statusPagamentoCartaoCredito : statusPagamentosCartaoCredito) {
	    		asaasStatusPagamentoCartaoCreditoRepository
	    		    .marcarPagamentoCartaoCreditoComoPagoPorId(statusPagamentoCartaoCredito.getId());
	    	}
	    	
	        // Marcando a venda como finalizada, pois todos os pagamentos foram confirmados
	    	vendaCompraLojaVirtualRepository
	    	    .marcarVendaCompraLojaVirtualComoFinalizadaPorId(vendaCompraLojaVirtual.getId());
	    	
	        // Retornando uma resposta de sucesso
	    	return new ResponseEntity<String>("Sucesso", HttpStatus.OK);
	    } else {
	        // Retornando uma resposta indicando que o pagamento não pôde ser finalizado com base no status recebido
	    	return new ResponseEntity<String>(
	    	    "O pagamento não pode ser finalizado: Status: " 
	    	    + cobrancaGeradaCartaoCreditoDTO.getStatus(), HttpStatus.OK);
	    }
	}
	
}
