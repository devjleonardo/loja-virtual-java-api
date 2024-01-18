package com.joseleonardo.lojavirtual.api.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joseleonardo.lojavirtual.api.dto.relatorio.RelatorioVendaCompraLojaVirtualPorStatusDTO;
import com.joseleonardo.lojavirtual.api.dto.venda.ItemVendaLojaDTO;
import com.joseleonardo.lojavirtual.api.dto.venda.VendaCompraLojaVirtualDTO;
import com.joseleonardo.lojavirtual.api.integracao.frete.melhor_envio.constants.MelhorEnvioFreteConstants;
import com.joseleonardo.lojavirtual.api.integracao.frete.melhor_envio.dto.EmpresaTransporteDTO;
import com.joseleonardo.lojavirtual.api.integracao.frete.melhor_envio.dto.cotacao.MelhorEnvioCalculoFreteDTO;
import com.joseleonardo.lojavirtual.api.integracao.frete.melhor_envio.dto.criandoenvios.MelhorEnvioCriandoEnviosProductsDTO;
import com.joseleonardo.lojavirtual.api.integracao.frete.melhor_envio.dto.criandoenvios.MelhorEnvioCriandoEnviosTagsDTO;
import com.joseleonardo.lojavirtual.api.integracao.frete.melhor_envio.dto.criandoenvios.MelhorEnvioCriandoEnviosVolumesDTO;
import com.joseleonardo.lojavirtual.api.integracao.frete.melhor_envio.dto.criandoenvios.MelhorEnvioInsercaoFretesCarrinhoDTO;
import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.ContaReceber;
import com.joseleonardo.lojavirtual.model.Endereco;
import com.joseleonardo.lojavirtual.model.ItemVendaLoja;
import com.joseleonardo.lojavirtual.model.PessoaFisica;
import com.joseleonardo.lojavirtual.model.PessoaJuridica;
import com.joseleonardo.lojavirtual.model.Produto;
import com.joseleonardo.lojavirtual.model.StatusRastreio;
import com.joseleonardo.lojavirtual.model.VendaCompraLojaVirtual;
import com.joseleonardo.lojavirtual.model.enums.StatusContaReceber;
import com.joseleonardo.lojavirtual.repository.ContaReceberRepository;
import com.joseleonardo.lojavirtual.repository.EnderecoRepository;
import com.joseleonardo.lojavirtual.repository.NotaFiscalVendaRepository;
import com.joseleonardo.lojavirtual.repository.PessoaFisicaRepository;
import com.joseleonardo.lojavirtual.repository.PessoaJuridicaRepository;
import com.joseleonardo.lojavirtual.repository.ProdutoRepository;
import com.joseleonardo.lojavirtual.repository.VendaCompraLojaVirtualRepository;
import com.joseleonardo.lojavirtual.service.EnvioEmailService;
import com.joseleonardo.lojavirtual.service.VendaCompraLojaVirtualService;

import okhttp3.OkHttpClient;

@RestController
public class VendaCompraLojaVirtualController {

	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaFisicaController pessoaFisicaController;
	
	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;
	
	@Autowired
	private VendaCompraLojaVirtualService vendaCompraLojaVirtualService;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Autowired
	private ContaReceberRepository contaReceberRepository;
	
	@Autowired
	private EnvioEmailService envioEmailService;
	
	@Autowired
	private JdbcTemplate jdbcTemplate; 
	
	@ResponseBody
	@PostMapping(value = "**/salvarVendaCompraLojaVirtual")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaCompraLojaVirtual(
	        @RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) 
					throws LojaVirtualException, UnsupportedEncodingException, MessagingException {
		Long empresaId = vendaCompraLojaVirtual.getEmpresa().getId();
		
		PessoaJuridica empresa = pessoaJuridicaRepository
		        .findById(empresaId).orElse(null);
		
		if (empresa == null) {
			throw new LojaVirtualException("Não econtrou nenhuma empresa com o código: " 
		            + empresaId);
		}
		
		vendaCompraLojaVirtual.setEmpresa(empresa);
		
		vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		PessoaFisica cliente = pessoaFisicaController.salvarPessoaFisica(vendaCompraLojaVirtual.getPessoa()).getBody();
		vendaCompraLojaVirtual.setPessoa(cliente);
		
		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(cliente);
		vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);
		
		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(cliente);
		vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);
		
		vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		
		for (int i = 0; i <vendaCompraLojaVirtual.getItensVendaLoja().size(); i++) {
			vendaCompraLojaVirtual.getItensVendaLoja().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			vendaCompraLojaVirtual.getItensVendaLoja().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		}
		
		/* Salva primeiro a venda e todos os dados */
		vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.saveAndFlush(vendaCompraLojaVirtual);
		
		/* Associa a venda gravada no banco com a nota fiscal */
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		
		/* Salva a nota fiscal novamente pra ficar amarrada na venda */
		notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());
		
		VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
		vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
		vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
		vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		
		for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItensVendaLoja()) {
			ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
			itemVendaLojaDTO.setQuantidade(itemVendaLoja.getQuantidade());
			itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
			
			vendaCompraLojaVirtualDTO.getItensVendaLojaDTO().add(itemVendaLojaDTO);
		}
		
		ContaReceber contaReceber = new ContaReceber();
		contaReceber.setDescricao("Venda da loja virtual nº: " + vendaCompraLojaVirtual.getId());
		contaReceber.setStatus(StatusContaReceber.QUITADA);
		contaReceber.setDataVencimento(Calendar.getInstance().getTime());
		contaReceber.setDataPagamento(Calendar.getInstance().getTime());
		contaReceber.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		contaReceber.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		contaReceber.setPessoa(vendaCompraLojaVirtual.getPessoa());
		contaReceber.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		
		contaReceberRepository.save(contaReceber);
		
		/* Email para o cliente */
		StringBuilder mensagemEmailParaCliente = new StringBuilder();
		mensagemEmailParaCliente.append("Olá, ").append(cliente.getNome()).append("<br/>");
		mensagemEmailParaCliente.append("Você realizou a compra de nº: ")
		        .append(vendaCompraLojaVirtual.getId())
	            .append("<br/>");
		mensagemEmailParaCliente.append("Na loja: ")
		        .append(vendaCompraLojaVirtual.getEmpresa().getNomeFantasia());
		
		/* Assunto, mensagem, destino */
		envioEmailService.enviarEmailHtml("Compra realizada", mensagemEmailParaCliente.toString(), 
				cliente.getEmail());
		
		/* Email para o vendedor (loja) */
		StringBuilder mensagemEmailParaLoja = new StringBuilder();
		mensagemEmailParaLoja.append("Você realizou uma venda, nº ")
		        .append(vendaCompraLojaVirtual.getId());
		
		/* Assunto, mensagem, destino */
		envioEmailService.enviarEmailHtml("Venda realizada", mensagemEmailParaLoja.toString(), 
				vendaCompraLojaVirtual.getEmpresa().getEmail());
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(vendaCompraLojaVirtualDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deletarVendaCompraLojaVirtualPorId/{id}")
	public ResponseEntity<?> deletarVendaCompraLojaVirtualPorId(@PathVariable("id") Long id) {
		if (!vendaCompraLojaVirtualRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("A venda de código "
					+ id + " já foi removida ou não existe", HttpStatus.OK);
		}
		
		vendaCompraLojaVirtualService.deletarVendaCompraLojaVirtualPorId(id);
		
		return new ResponseEntity<>("Venda removida", HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/desativarVendaCompraLojaVirtualPorIdAtravesDaExclusaoLogica/{id}")
	public ResponseEntity<?> desativarVendaCompraLojaVirtualPorIdAtravesDaExclusaoLogica(
			@PathVariable("id") Long id) {
		if (!vendaCompraLojaVirtualRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("A venda de código "
					+ id + " já foi desativada por exclusão lógica ou não existe", HttpStatus.OK);
		}
		
		vendaCompraLojaVirtualService
		        .desativarVendaCompraLojaVirtualPorIdAtravesDaExclusaoLogica(id);
		
		return new ResponseEntity<>("Venda desativada com sucesso através da exclusão lógica", 
				HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/ativarVendaCompraLojaVirtualPorIdDesativadaAtravesDaExclusaoLogica/{id}")
	public ResponseEntity<?> ativarVendaCompraLojaVirtualPorIdDesativadaAtravesDaExclusaoLogica(
			@PathVariable("id") Long id) {
		if (!vendaCompraLojaVirtualRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("A venda de código "
					+ id + " já foi removida ou não existe", HttpStatus.OK);
		}
		
		vendaCompraLojaVirtualService
		        .ativarVendaCompraLojaVirtualPorIdDesativadaAtravesDaExclusaoLogica(id);
		
		return new ResponseEntity<>("Venda que foi desativada por causa da exclusão lógica, "
				+ "foi ativada com sucesso", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarVendaCompraLojaVirtualPorId/{id}")
	public ResponseEntity<VendaCompraLojaVirtualDTO> buscarVendaCompraLojaVirtualPorId(
			@PathVariable("id") Long id) throws LojaVirtualException {
		VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = 
				vendaCompraLojaVirtualService.buscarVendaCompraPorId(id);
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(vendaCompraLojaVirtualDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarVendaCompraLojaVirtualPorProdutoId/{produtoId}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscarVendaCompraLojaVirtualPorProdutoId(
			@PathVariable("produtoId") Long produtoId) throws LojaVirtualException {
		Produto produto = produtoRepository.findById(produtoId).orElse(null);
		
		if (produto == null) {
			throw new LojaVirtualException("Não econtrou nenhum produto com o código: " 
		            + produtoId);
		}
		
		List<VendaCompraLojaVirtual> vendasCompraLojaVirtual = vendaCompraLojaVirtualRepository
		        .buscarVendaCompraLojaVirtualPorProdutoId(produtoId);
		
		List<VendaCompraLojaVirtualDTO> vendasCompraLojaVirtualDTO = new ArrayList<>();
		
		for (VendaCompraLojaVirtual vendaCompraLojaVirtual : vendasCompraLojaVirtual) {
			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
			vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
			vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
			vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
			vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
			vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
			
			for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItensVendaLoja()) {
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
				itemVendaLojaDTO.setQuantidade(itemVendaLoja.getQuantidade());
				itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
				
				vendaCompraLojaVirtualDTO.getItensVendaLojaDTO().add(itemVendaLojaDTO);
			}
			
			vendasCompraLojaVirtualDTO.add(vendaCompraLojaVirtualDTO);
		}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendasCompraLojaVirtualDTO, 
				HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarVendaCompraLojaVirtualDeFormaDinamica/{valor}/{tipoConsulta}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscarVendaCompraLojaVirtualDeFormaDinamica(
			@PathVariable("valor") String valor, 
			@PathVariable("tipoConsulta") String tipoConsulta) throws LojaVirtualException {
		List<VendaCompraLojaVirtual> vendasCompraLojaVirtual = new ArrayList<>();
		
		if (tipoConsulta.equalsIgnoreCase("POR_PRODUTO_ID")) {
			long produtoId = Long.parseLong(valor);
			
			Produto produto = produtoRepository.findById(produtoId).orElse(null);
			
			if (produto == null) {
				throw new LojaVirtualException("Não econtrou nenhum produto com o código: " 
			            + valor);
			}
			
			vendasCompraLojaVirtual = vendaCompraLojaVirtualRepository
			        .buscarVendaCompraLojaVirtualPorProdutoId(produtoId);
		} else if (tipoConsulta.equalsIgnoreCase("POR_NOME_DO_PRODUTO")) {
			vendasCompraLojaVirtual = vendaCompraLojaVirtualRepository
			        .buscarVendaCompraLojaVirtualPorNomeDoProduto(valor.trim().toUpperCase());
		} else if (tipoConsulta.equalsIgnoreCase("POR_NOME_DO_CLIENTE")) {
			vendasCompraLojaVirtual = vendaCompraLojaVirtualRepository
			        .buscarVendaCompraLojaVirtualPorNomeDoCliente(valor.trim().toUpperCase());
		} else if (tipoConsulta.equalsIgnoreCase("POR_ENDERECO_DE_ENTREGA")) {
			vendasCompraLojaVirtual = vendaCompraLojaVirtualRepository
			        .buscarVendaCompraLojaVirtualPorEnderecoDeEntrega(valor.trim().toUpperCase());
		} else if (tipoConsulta.equalsIgnoreCase("POR_ENDERECO_DE_COBRANCA")) {
			vendasCompraLojaVirtual = vendaCompraLojaVirtualRepository
			        .buscarVendaCompraLojaVirtualPorEnderecoDeCobranca(valor.trim().toUpperCase());
		}
		
		List<VendaCompraLojaVirtualDTO> vendasCompraLojaVirtualDTO = new ArrayList<>();
		
		for (VendaCompraLojaVirtual vendaCompraLojaVirtual : vendasCompraLojaVirtual) {
			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
			vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
			vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
			vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
			vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
			vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
			
			for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItensVendaLoja()) {
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
				itemVendaLojaDTO.setQuantidade(itemVendaLoja.getQuantidade());
				itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
				
				vendaCompraLojaVirtualDTO.getItensVendaLojaDTO().add(itemVendaLojaDTO);
			}
			
			vendasCompraLojaVirtualDTO.add(vendaCompraLojaVirtualDTO);
		}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendasCompraLojaVirtualDTO, 
				HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarVendaCompraLojaVirtualPorIntervaloDeDatasDaVenda/{data1}/{data2}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscarVendaCompraLojaVirtualPorIntervaloDeDatasDaVenda(
			@PathVariable("data1") String data1, 
			@PathVariable("data2") String data2) throws LojaVirtualException, ParseException {
		List<VendaCompraLojaVirtual> vendasCompraLojaVirtual = new ArrayList<>();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date data1Formatada = simpleDateFormat.parse(data1);
		Date data2Formatada = simpleDateFormat.parse(data2);
		
		
		vendasCompraLojaVirtual = vendaCompraLojaVirtualRepository
		        .buscarVendaCompraLojaVirtualPorIntervaloDeDatasDaVenda(data1Formatada, data2Formatada);
		
		List<VendaCompraLojaVirtualDTO> vendasCompraLojaVirtualDTO = new ArrayList<>();
		
		for (VendaCompraLojaVirtual vendaCompraLojaVirtual : vendasCompraLojaVirtual) {
			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
			vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
			vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
			vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
			vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
			vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
			
			for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItensVendaLoja()) {
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
				itemVendaLojaDTO.setQuantidade(itemVendaLoja.getQuantidade());
				itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
				
				vendaCompraLojaVirtualDTO.getItensVendaLojaDTO().add(itemVendaLojaDTO);
			}
			
			vendasCompraLojaVirtualDTO.add(vendaCompraLojaVirtualDTO);
		}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendasCompraLojaVirtualDTO, 
				HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarVendaCompraLojaVirtualPorClienteId/{clienteId}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscarVendaCompraLojaVirtualPorClienteId(
			@PathVariable("clienteId") Long clienteId) throws LojaVirtualException {
		if (!pessoaFisicaRepository.findById(clienteId).isPresent()) {
			throw new LojaVirtualException("Não econtrou nenhum cliente com o código: " 
		            + clienteId);
		}
		
		List<VendaCompraLojaVirtual> vendasCompraLojaVirtual = vendaCompraLojaVirtualRepository
				.buscarVendaCompraLojaVirtualPorClienteId(clienteId);
		
		List<VendaCompraLojaVirtualDTO> vendasCompraLojaVirtualDTO = new ArrayList<>();
		
		for (VendaCompraLojaVirtual vendaCompraLojaVirtual : vendasCompraLojaVirtual) {
			VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
			vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
			vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
			vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
			vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
			vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
			vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
			vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
			
			for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItensVendaLoja()) {
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
				itemVendaLojaDTO.setQuantidade(itemVendaLoja.getQuantidade());
				itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
				
				vendaCompraLojaVirtualDTO.getItensVendaLojaDTO().add(itemVendaLojaDTO);
			}
			
			vendasCompraLojaVirtualDTO.add(vendaCompraLojaVirtualDTO);
		}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(vendasCompraLojaVirtualDTO, 
				HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "**/calculoDeFreteLojaVirtual")
	public ResponseEntity<List<EmpresaTransporteDTO>> calculoDeFreteLojaVirtual(
			@RequestBody @Valid MelhorEnvioCalculoFreteDTO calculoFreteDTO) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(calculoFreteDTO);
		
		OkHttpClient client = new OkHttpClient();

		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);
		okhttp3.Request request = new okhttp3.Request.Builder()
		  .url(MelhorEnvioFreteConstants.MELHOR_ENVIO_URL_SANDABOX + "/api/v2/me/shipment/calculate")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + MelhorEnvioFreteConstants.MELHOR_ENVIO_ACCESS_TOKEN_SANDBOX)
		  .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		  .build();

		okhttp3.Response response = client.newCall(request).execute();
		
		JsonNode retornoDaApi = new ObjectMapper().readTree(response.body().string());
		
		Iterator<JsonNode> camposDaApi = retornoDaApi.iterator();
		
		List<EmpresaTransporteDTO> empresasTransporte = new ArrayList<>();
		
		while (camposDaApi.hasNext()) {
			JsonNode campoApi = camposDaApi.next();
			
			EmpresaTransporteDTO empresaTransporte = new EmpresaTransporteDTO();
			
			if (campoApi.get("id") != null) {
				empresaTransporte.setId(campoApi.get("id").asText());
			}
			
			if (campoApi.get("name") != null) {
				empresaTransporte.setNome(campoApi.get("name").asText());
			}
			
			if (campoApi.get("price") != null) {
				empresaTransporte.setValor(campoApi.get("price").asText());
			}
			
			if (campoApi.get("company") != null) {
				empresaTransporte.setEmpresa(campoApi.get("company").get("name").asText());
				empresaTransporte.setFoto(campoApi.get("company").get("picture").asText());
			}
			
			if(empresaTransporte.dadosOk()) {
				empresasTransporte.add(empresaTransporte);
			}
		}
		
		return new ResponseEntity<List<EmpresaTransporteDTO>>(empresasTransporte, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/gerarEtiquetaEnvioFrete/{vendaId}")
	public ResponseEntity<String> gerarEtiquetaEnvioFrete (
	        @PathVariable("vendaId") Long vendaId) throws LojaVirtualException, IOException, SQLException {
		VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
				.findById(vendaId).orElse(null);
		
		if (vendaCompraLojaVirtual == null) {
			throw new LojaVirtualException("Não econtrou nenhuma venda com o código: " + vendaId);
		}
		
		List<Endereco> enderecos = enderecoRepository
		    .buscarEnderecoPorPessoaId(vendaCompraLojaVirtual.getPessoa().getId());
		
		vendaCompraLojaVirtual.getEmpresa().setEnderecos(enderecos);
		
		MelhorEnvioInsercaoFretesCarrinhoDTO insercaoFretesNoCarrinhoDTO = new MelhorEnvioInsercaoFretesCarrinhoDTO();
		insercaoFretesNoCarrinhoDTO.setService(
		    vendaCompraLojaVirtual.getIdServicoTransportadora()
		);
		insercaoFretesNoCarrinhoDTO.setAgency("49");
		
		PessoaJuridica empresa = vendaCompraLojaVirtual.getEmpresa();
		insercaoFretesNoCarrinhoDTO.getFrom().setName(empresa.getNomeFantasia());
		insercaoFretesNoCarrinhoDTO.getFrom().setPhone(empresa.getTelefone());
		insercaoFretesNoCarrinhoDTO.getFrom().setEmail(empresa.getEmail());
		insercaoFretesNoCarrinhoDTO.getFrom().setCompany_document(empresa.getCnpj());
		insercaoFretesNoCarrinhoDTO.getFrom().setState_register(empresa.getInscricaoEstadual());
		
		Endereco endereco1Empresa = empresa.getEnderecos().get(0);
		insercaoFretesNoCarrinhoDTO.getFrom().setAddress(endereco1Empresa.getLogradouro());
		insercaoFretesNoCarrinhoDTO.getFrom().setComplement(endereco1Empresa.getComplemento());
		insercaoFretesNoCarrinhoDTO.getFrom().setNumber(endereco1Empresa.getNumero());
		insercaoFretesNoCarrinhoDTO.getFrom().setDistrict(endereco1Empresa.getBairro());
		insercaoFretesNoCarrinhoDTO.getFrom().setCity(endereco1Empresa.getCidade());
		insercaoFretesNoCarrinhoDTO.getFrom().setCountry_id("BR");
		insercaoFretesNoCarrinhoDTO.getFrom().setPostal_code(endereco1Empresa.getCep());
		insercaoFretesNoCarrinhoDTO.getFrom().setState_abbr(endereco1Empresa.getUf());
		insercaoFretesNoCarrinhoDTO.getFrom().setNote("Não há");
		
		PessoaFisica cliente = vendaCompraLojaVirtual.getPessoa();
		insercaoFretesNoCarrinhoDTO.getTo().setName(cliente.getNome());
		insercaoFretesNoCarrinhoDTO.getTo().setPhone(cliente.getTelefone());
		insercaoFretesNoCarrinhoDTO.getTo().setEmail(cliente.getEmail());
		insercaoFretesNoCarrinhoDTO.getTo().setDocument(cliente.getCpf());
		
		Endereco enderecoEntregaCliente = cliente.getEnderecoEntrega();
		insercaoFretesNoCarrinhoDTO.getTo().setAddress(enderecoEntregaCliente.getLogradouro());
		insercaoFretesNoCarrinhoDTO.getTo().setComplement(enderecoEntregaCliente.getComplemento());
		insercaoFretesNoCarrinhoDTO.getTo().setNumber(enderecoEntregaCliente.getNumero());
		insercaoFretesNoCarrinhoDTO.getTo().setDistrict(enderecoEntregaCliente.getBairro());
		insercaoFretesNoCarrinhoDTO.getTo().setCity(enderecoEntregaCliente.getCidade());
		insercaoFretesNoCarrinhoDTO.getTo().setCountry_id("BR");
		insercaoFretesNoCarrinhoDTO.getTo().setPostal_code(enderecoEntregaCliente.getCep());
		insercaoFretesNoCarrinhoDTO.getTo().setState_abbr(enderecoEntregaCliente.getUf());
		insercaoFretesNoCarrinhoDTO.getTo().setNote("Não há");
		
		List<MelhorEnvioCriandoEnviosProductsDTO> products = new ArrayList<>();
		
		for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItensVendaLoja()) {
			MelhorEnvioCriandoEnviosProductsDTO product = new MelhorEnvioCriandoEnviosProductsDTO();
			product.setName(itemVendaLoja.getProduto().getNome());
			product.setQuantity(itemVendaLoja.getQuantidade().toString());
			product.setUnitary_value(itemVendaLoja.getProduto().getValorVenda().toString());
			
			products.add(product);
		}
		
		insercaoFretesNoCarrinhoDTO.setProducts(products);
		
		List<MelhorEnvioCriandoEnviosVolumesDTO> volumes = new ArrayList<>();
		
		for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItensVendaLoja()) {
			MelhorEnvioCriandoEnviosVolumesDTO volume = new MelhorEnvioCriandoEnviosVolumesDTO();
			volume.setHeight(itemVendaLoja.getProduto().getAltura().toString());
			volume.setWidth(itemVendaLoja.getProduto().getLargura().toString());
			volume.setLength(itemVendaLoja.getProduto().getProfundidade().toString());
			volume.setWeight(itemVendaLoja.getProduto().getPeso().toString());
			
			volumes.add(volume);
		}
		
		insercaoFretesNoCarrinhoDTO.setVolumes(volumes);
		
		insercaoFretesNoCarrinhoDTO.getOptions()
	        .setInsurance_value(vendaCompraLojaVirtual.getValorTotal().toString());
		insercaoFretesNoCarrinhoDTO.getOptions().setReceipt(false);
		insercaoFretesNoCarrinhoDTO.getOptions().setOwn_hand(false);
		insercaoFretesNoCarrinhoDTO.getOptions().setReverse(false);
		insercaoFretesNoCarrinhoDTO.getOptions().setNon_commercial(false);
		insercaoFretesNoCarrinhoDTO.getOptions().getInvoice()
	        .setKey(vendaCompraLojaVirtual.getNotaFiscalVenda().getNumero());
		insercaoFretesNoCarrinhoDTO.getOptions()
		    .setPlataform(vendaCompraLojaVirtual.getEmpresa().getNomeFantasia());
		
		MelhorEnvioCriandoEnviosTagsDTO tag = new MelhorEnvioCriandoEnviosTagsDTO();
		tag.setTag("Identificação do pedido na plataforma, exemplo: " 
		    + vendaCompraLojaVirtual.getId());
		tag.setUrl(null);
		
		insercaoFretesNoCarrinhoDTO.getOptions().getTags().add(tag);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonEnvio = objectMapper.writeValueAsString(insercaoFretesNoCarrinhoDTO);
		
		OkHttpClient clientInserirFreteNoCarrinho = new OkHttpClient();
		
		okhttp3.MediaType mediaTypeInserirFreteNoCarrinho = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody bodyFreteNoCarrinho = okhttp3.RequestBody.create(mediaTypeInserirFreteNoCarrinho, jsonEnvio);
		
		okhttp3.Request request = new okhttp3.Request.Builder()
		    .url(MelhorEnvioFreteConstants.MELHOR_ENVIO_URL_SANDABOX + "/api/v2/me/cart")
		    .post(bodyFreteNoCarrinho)
		    .addHeader("Accept", "application/json")
		    .addHeader("Content-Type", "application/json")
		    .addHeader(
		         "Authorization", "Bearer " 
		        + MelhorEnvioFreteConstants.MELHOR_ENVIO_ACCESS_TOKEN_SANDBOX
		    )
		    .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		    .build();
		
		okhttp3.Response responseInserirFreteNoCarrinho = clientInserirFreteNoCarrinho.newCall(request).execute();
		
		String respostaJson = responseInserirFreteNoCarrinho.body().string();
		
		if (respostaJson.contains("error")) {
			throw new LojaVirtualException(respostaJson);
		}
		
		JsonNode retornoDaApi = new ObjectMapper().readTree(respostaJson);
		
		Iterator<JsonNode> camposDaApi = retornoDaApi.iterator();
		
		String idEtiquetaEnvioFrete = "";
		
		while (camposDaApi.hasNext()) {
			JsonNode campoApi = camposDaApi.next();
			
			if (campoApi.textValue() != null) {
				idEtiquetaEnvioFrete = campoApi.textValue();
			} else {
				idEtiquetaEnvioFrete = campoApi.asText();
			}
			
			break;
		}
		
		vendaCompraLojaVirtual.setIdEtiquetaEnvioFrete(idEtiquetaEnvioFrete);
		
		/* Salvando o id da etiqueta de envio do frete retornado da API do Melhor Envio */
		jdbcTemplate.execute(
				  "BEGIN; "
				+ "UPDATE venda_compra_loja_virtual "
				+ "SET id_etiqueta_envio_frete = '" + idEtiquetaEnvioFrete + "' "
			    + "WHERE id = " + vendaCompraLojaVirtual.getId() + "; "
				+ "COMMIT;");
		
		OkHttpClient clientCompraDeFretes = new OkHttpClient();

		okhttp3.MediaType mediaTypeCompraDeFretes = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody bodyCompraDeFretes = okhttp3.RequestBody.create(
		    mediaTypeCompraDeFretes, 
			"{\"orders\":[\"" + idEtiquetaEnvioFrete + "\"]}"
		);
		
		okhttp3.Request requestCompraDeFretes = new okhttp3.Request.Builder()
		    .url(MelhorEnvioFreteConstants.MELHOR_ENVIO_URL_SANDABOX + "/api/v2/me/shipment/checkout")
		    .post(bodyCompraDeFretes)
		    .addHeader("Accept", "application/json")
		    .addHeader("Content-Type", "application/json")
		    .addHeader(
		        "Authorization", "Bearer " 
		        + MelhorEnvioFreteConstants.MELHOR_ENVIO_ACCESS_TOKEN_SANDBOX
		    )
		    .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		    .build();

		okhttp3.Response responseCompraDeFretes = clientCompraDeFretes
		    .newCall(requestCompraDeFretes).execute();
		
		if (!responseCompraDeFretes.isSuccessful()) {
			throw new LojaVirtualException("Não foi possível realizar a compra de frete");
		}
		
		OkHttpClient clientGeracaoDeEtiquetas = new OkHttpClient();

		okhttp3.MediaType mediaTypeGeracaoDeEtiquetas = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody bodyGeracaoDeEtiquetas = okhttp3.RequestBody.create(mediaTypeGeracaoDeEtiquetas, "{\"orders\":[\"" + idEtiquetaEnvioFrete + "\"]}");
		
		okhttp3.Request requestGeracaoDeEtiquetas = new okhttp3.Request.Builder()
		    .url(MelhorEnvioFreteConstants.MELHOR_ENVIO_URL_SANDABOX + "/api/v2/me/shipment/generate")
		    .post(bodyGeracaoDeEtiquetas)
		    .addHeader("Accept", "application/json")
		    .addHeader("Content-Type", "application/json")
		    .addHeader(
		         "Authorization", "Bearer " 
		        + MelhorEnvioFreteConstants.MELHOR_ENVIO_ACCESS_TOKEN_SANDBOX
		     )
		    .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		    .build();

		okhttp3.Response responseGeracaoDeEtiquetas = clientGeracaoDeEtiquetas
		    .newCall(requestGeracaoDeEtiquetas)
		    .execute();
		
		if (!responseGeracaoDeEtiquetas.isSuccessful()) {
			throw new LojaVirtualException("Não foi possível realizar a geração de etiqueta");
		}
		
		OkHttpClient clientImpressaoDeEtiquetas = new OkHttpClient();

		okhttp3.MediaType mediaTypeImpressaoDeEtiquetas = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody bodyImpressaoDeEtiquetas = okhttp3.RequestBody.create(mediaTypeImpressaoDeEtiquetas, "{\"mode\":\"private\",\"orders\":[\"" + idEtiquetaEnvioFrete + "\"]}");
		
		okhttp3.Request requestImpressaoDeEtiquetas = new okhttp3.Request.Builder()
		    .url(MelhorEnvioFreteConstants.MELHOR_ENVIO_URL_SANDABOX + "/api/v2/me/shipment/print")
		    .post(bodyImpressaoDeEtiquetas)
		    .addHeader("Accept", "application/json")
		    .addHeader("Content-Type", "application/json")
		    .addHeader(
		        "Authorization", "Bearer " 
		        + MelhorEnvioFreteConstants.MELHOR_ENVIO_ACCESS_TOKEN_SANDBOX
		    )
		    .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		    .build();

		okhttp3.Response responseImpressaoDeEtiquetas = clientImpressaoDeEtiquetas
		    .newCall(requestImpressaoDeEtiquetas)
		    .execute();
		
		if (!responseImpressaoDeEtiquetas.isSuccessful()) {
			throw new LojaVirtualException("Não foi possível realizar a impressão de etiqueta");
		}
		
		String urlImpressaoEtiquetaEnvioFrete = responseImpressaoDeEtiquetas.body().string();
		
		jdbcTemplate.execute(
				  "BEGIN; "
				+ "UPDATE venda_compra_loja_virtual "
				+ "SET url_impressao_etiqueta_envio_frete = '" 
				+     urlImpressaoEtiquetaEnvioFrete + "' " 
				+ "WHERE id = " + vendaCompraLojaVirtual.getId() + "; "
				+ "COMMIT;");
		
		OkHttpClient clientRastreio = new OkHttpClient();

		okhttp3.MediaType mediaTypeRastreio = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody bodyRastreio = okhttp3.RequestBody.create(mediaTypeRastreio , "{\"orders\":[\"" + idEtiquetaEnvioFrete + "\"]}");
		
		okhttp3.Request requestRastreio = new okhttp3.Request.Builder()
		    .url(MelhorEnvioFreteConstants.MELHOR_ENVIO_URL_SANDABOX + "/api/v2/me/shipment/tracking")
		    .post(bodyRastreio)
		    .addHeader("Accept", "application/json")
		    .addHeader("Content-type", "application/json")
		    .addHeader(
		    	"Authorization", "Bearer " 
		        + MelhorEnvioFreteConstants.MELHOR_ENVIO_ACCESS_TOKEN_SANDBOX
		    )
		    .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		    .build();

		okhttp3.Response responseRastreio = clientRastreio.newCall(requestRastreio).execute();
		
		JsonNode retornoDaApiRastreio = new ObjectMapper().readTree(responseRastreio.body().string());
		
		Iterator<JsonNode> camposDaApiRastreio = retornoDaApiRastreio.iterator();
		
		String codigoRastreamentoFrete = "";
		
		while (camposDaApiRastreio.hasNext()) {
			JsonNode campoApiRastreio = camposDaApiRastreio.next();
			
			if (campoApiRastreio.get("tracking") != null) {
				codigoRastreamentoFrete = campoApiRastreio.get("tracking").asText();
			} else {
				codigoRastreamentoFrete = campoApiRastreio.asText();
			}
			
			break;
		}
		
		String sqlSelectStatusRastreios = 
				  "SELECT * FROM status_rastreio "
				+ "WHERE venda_compra_loja_virtual_id = " + vendaId;
				
		List<StatusRastreio> rastreios = jdbcTemplate.query(
		    sqlSelectStatusRastreios,
		    new BeanPropertyRowMapper<>(StatusRastreio.class)
		);	
		
		if (rastreios.isEmpty()) {
			jdbcTemplate.execute(
					  "BEGIN; "
					+ "INSERT INTO status_rastreio"
					+ "(url_rastreio, venda_compra_loja_virtual_id, empresa_id) "
					+ "VALUES (" 
					+ "    'https://app.melhorrastreio.com.br/app/" + codigoRastreamentoFrete + "', " 
					+      vendaCompraLojaVirtual.getId() + ", "
					+      vendaCompraLojaVirtual.getEmpresa().getId()  
					+ "); "
					+ "COMMIT;");
		} else {
			jdbcTemplate.execute(
					  "BEGIN; "
					+ "UPDATE status_rastreio SET url_rastreio = "
					+ "    'https://app.melhorrastreio.com.br/app/" + codigoRastreamentoFrete + "' " 
					+ "WHERE venda_compra_loja_virtual_id = " 
					+      vendaCompraLojaVirtual.getId() + "; "
					+ "COMMIT;");
		}
		
		return new ResponseEntity<String>("Sucesso", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/cancelarEtiquetas/{etiquetaId}/{descricao}")
	public ResponseEntity<String> cancelarEtiquetas(
			@PathVariable("etiquetaId") String etiquetaId, 
			@PathVariable("descricao") String descricao) throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder() .build();
		
		okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
		okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{\n\"order\":{\n\"id\":\"" + etiquetaId + "\",\n\"reason_id\":\"" + 2 + "\",\n\"description\":\"" + descricao + "\"\n}\n}");
		okhttp3.Request request = new okhttp3.Request.Builder()
	      .url(MelhorEnvioFreteConstants.MELHOR_ENVIO_URL_SANDABOX + "/api/v2/me/shipment/cancel")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader(
			  "Authorization", "Bearer " 
		      + MelhorEnvioFreteConstants.MELHOR_ENVIO_ACCESS_TOKEN_SANDBOX
		  )
		  .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		  .build();
		
		okhttp3.Response response = client.newCall(request).execute();
		
		return new ResponseEntity<String>(response.body().string(), HttpStatus.OK);
	}


	@ResponseBody
	@PostMapping(value = "**/relatorioVendaCompraLojaVirtualPorStatus")
	public ResponseEntity<List<RelatorioVendaCompraLojaVirtualPorStatusDTO>> relatorioVendaCompraLojaVirtualPorStatus(
			@Valid @RequestBody RelatorioVendaCompraLojaVirtualPorStatusDTO relatorioVendaCompraLojaVirtualPorStatusDTO) {
		List<RelatorioVendaCompraLojaVirtualPorStatusDTO> retorno = new ArrayList<>();
		
		retorno = vendaCompraLojaVirtualService
				      .gerarRelatorioVendaCompraLojaVirtualPorStatus(relatorioVendaCompraLojaVirtualPorStatusDTO);
		
		return new ResponseEntity<List<RelatorioVendaCompraLojaVirtualPorStatusDTO>>(
		    retorno, HttpStatus.OK);
	}
	
}
