package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.joseleonardo.lojavirtual.api.dto.venda.VendaCobrancaDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.constants.AsaasPagamentoConstants;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.criar_nova_cobranca.AsaasPagamentoCriarNovaCobrancaDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.criar_novo_cliente.AsaasPagamentoCriarNovoClienteDTO;
import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.VendaCompraLojaVirtual;
import com.joseleonardo.lojavirtual.repository.VendaCompraLojaVirtualRepository;
import com.joseleonardo.lojavirtual.ssl.HostIgnoringSSLClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class AsaasPagamentoService {
	
	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	public String criarNovaCobranca(VendaCobrancaDTO vendaCobrancaDTO) throws Exception {
	    // Buscando a venda com base no ID
	    VendaCompraLojaVirtual vendaCompraLojaVirtual =
	            vendaCompraLojaVirtualRepository.findById(vendaCobrancaDTO.getIdVenda()).orElse(null);

	    // Verificando se a venda foi encontrada
	    if (vendaCompraLojaVirtual == null) {
	        throw new LojaVirtualException(
	            "Venda não encontrada para o ID da venda fornecido: " + vendaCobrancaDTO.getIdVenda()
	        );
	    }

	    // Criando um DTO para a nova cobrança
	    AsaasPagamentoCriarNovaCobrancaDTO novaCobrancaDTO = new AsaasPagamentoCriarNovaCobrancaDTO();

	    // Configurando o cliente associado à cobrança (buscando ou criando)
	    novaCobrancaDTO.setCustomer(buscarOuCriarNovoCliente(vendaCobrancaDTO));

	    // Configurando a forma de pagamento da cobrança para ser em Pix ou Boleto
	    novaCobrancaDTO.setBillingType("BOLETO");

	    // Obtendo o valor da cobrança da venda
	    float valorDaCobranca = vendaCompraLojaVirtual.getValorTotal().floatValue();
	    novaCobrancaDTO.setValue(valorDaCobranca);
	    novaCobrancaDTO.setInstallmentValue(valorDaCobranca);

	    // Definindo a data de vencimento para 7 dias
	    Calendar dataVencimento = Calendar.getInstance();
	    dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
	    novaCobrancaDTO.setDueDate(new SimpleDateFormat("yyyy-MM-dd").format(dataVencimento.getTime()));

	    // Descrevendo a cobrança
	    novaCobrancaDTO.setDescription(
	            "Pix ou Boleto gerado para a cobrança de código: " + vendaCompraLojaVirtual.getId()
	    );

	    // Configurando outros detalhes da cobrança
	    novaCobrancaDTO.setInstallmentCount(1);
	    novaCobrancaDTO.getInterest().setValue(1F);
	    novaCobrancaDTO.getFine().setValue(1F);

	    // Convertendo o DTO para JSON
	    String json = new ObjectMapper().writeValueAsString(novaCobrancaDTO);

	    // Criando um cliente HTTP ignorando SSL
	    Client client = new HostIgnoringSSLClient(AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX)
	            .createUnsecuredHttpClient();

	    // Definindo o recurso da API da Asaas para criar uma nova cobrança
	    WebResource webResource = client.resource(
	            AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX + "payments"
	    );

	    // Enviando uma requisição POST para a API Asaas
	    ClientResponse clientResponse = webResource
	            .accept("application/json;charset=UTF-8")
	            .header("Content-Type", "application/json")
	            .header("access_token", AsaasPagamentoConstants.ASAAS_ACCESS_TOKEN_SANDBOX)
	            .post(ClientResponse.class, json);

	    // Obtendo a resposta da API
	    String respostaApi = clientResponse.getEntity(String.class);

	    // Fechando a resposta da API Asaas
	    clientResponse.close();

	    return respostaApi;
	}

	@SuppressWarnings("unchecked")
	public String buscarOuCriarNovoCliente(VendaCobrancaDTO vendaCobrancaDTO) throws Exception {
	    /* ID do cliente para ligar com a cobrança */
	    String idCliente = "";
	    
	    // Criando um cliente HTTP ignorando SSL
	    Client client = new HostIgnoringSSLClient(
	        AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX
	    ).createUnsecuredHttpClient();
	    
	    // Definindo o recurso da API da Asaas para buscar cliente pelo e-mail
	    WebResource webResource = client.resource(
	          AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX + "customers?email=" 
	        + vendaCobrancaDTO.getEmailCliente()
	    );
	    
	    // Enviando uma requisição GET para a API Asaas
	    ClientResponse clientResponse = webResource
	        .accept("application/json;charset=UTF-8")
	        .header("Content-Type", "application/json")
	        .header("access_token", AsaasPagamentoConstants.ASAAS_ACCESS_TOKEN_SANDBOX)
	        .get(ClientResponse.class);
	    
	    // Parser para converter a resposta JSON em um mapa
	    LinkedHashMap<String, Object> parser = new JSONParser(
	        clientResponse.getEntity(String.class)
	    ).parseObject();
	    
	    // Obtendo o número total de resultados da busca
	    Integer totalCount = Integer.parseInt(parser.get("totalCount").toString());

	    // Verifica se não há clientes encontrados com o e-mail fornecido
	    if (totalCount <= 0) {
	    	// Criando o DTO para criar um novo cliente na API da Asaas.
	    	AsaasPagamentoCriarNovoClienteDTO novoClienteDTO = new AsaasPagamentoCriarNovoClienteDTO();
	    	novoClienteDTO.setName(vendaCobrancaDTO.getNomeCliente());
			novoClienteDTO.setCpfCnpj(vendaCobrancaDTO.getCpfCnpjCliente());
			novoClienteDTO.setEmail(vendaCobrancaDTO.getEmailCliente());
			novoClienteDTO.setMobilePhone(vendaCobrancaDTO.getTelefoneCliente());
	    	
	        // Convertendo o objeto DTO para formato JSON
	        String json = new ObjectMapper().writeValueAsString(vendaCobrancaDTO);
	        
	        // Criando um cliente HTTP ignorando SSL
	        Client clientNovoCliente = new HostIgnoringSSLClient(
	            AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX
	        ).createUnsecuredHttpClient();
	        
	        // Definindo o recurso da API da Asaas para criar um novo cliente
	        WebResource webResourceNovoCliente = clientNovoCliente.resource(
	            AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX + "customers"
	        );
	        
	        // Enviando uma requisição POST para a API Asaas
	        ClientResponse clientResponseNovoCliente = webResourceNovoCliente
	            .accept("application/json;charset=UTF-8")
	            .header("Content-Type", "application/json")
	            .header("access_token", AsaasPagamentoConstants.ASAAS_ACCESS_TOKEN_SANDBOX)
	            .post(ClientResponse.class, json);
	        
	        // Obtendo a resposta da criação do cliente
	        LinkedHashMap<String, Object> parserNovCliente = new JSONParser(
	            clientResponseNovoCliente.getEntity(String.class)
	        ).parseObject();
	        
	        clientResponseNovoCliente.close();
	        
	        // Obtendo o ID do cliente recém-criado
	        idCliente = parserNovCliente.get("id").toString();
	    } else { 
	        // Obtendo os dados do cliente existente
	    	List<Object> dadosCliente = (List<Object>) parser.get("data");
	        
	        // Extraindo o ID do cliente do JSON
	        idCliente = new Gson().toJsonTree(
	            dadosCliente.get(0)
	        ).getAsJsonObject().get("id").toString().replaceAll("\"", "");
	    }
	    
	    // Fechando a resposta da API Asaas
	    clientResponse.close();
	    
	    // Retornando o ID do cliente
	    return idCliente;
	}

	/**
	 * Criar a chave Pix dentro da nossa conta Asaas.
	 * 
	 * @return Chave
	 * @throws Exception
	 */
	public String criarChavePix() throws Exception {
		// Criando um cliente HTTP ignorando SSL
		Client client = new HostIgnoringSSLClient(
		    AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX
		).createUnsecuredHttpClient();
		
		// Definindo o recurso da API para criar uma chave Pix
		WebResource webResource = client.resource(
		    AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX + "pix/addressKeys"
		);
		
		// Enviando uma requisição POST para a API GetResponse
		ClientResponse clientResponse = webResource
		    .accept("application/json;charset=UTF-8")
		    .header("Content-Type", "application/json")
		    .header("access_token", AsaasPagamentoConstants.ASAAS_ACCESS_TOKEN_SANDBOX)
		    .post(ClientResponse.class, "{\"type\":\"EVP\"}");
		
	    // Obtendo a resposta da API
	    String respostaApi = clientResponse.getEntity(String.class);
		
	    // Fechando a resposta do cliente
		clientResponse.close();
		
	    // Retornando a resposta da API Asaas
		return respostaApi;
	}
	
}