package com.joseleonardo.lojavirtual.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.Endereco;
import com.joseleonardo.lojavirtual.model.ItemVendaLoja;
import com.joseleonardo.lojavirtual.model.PessoaFisica;
import com.joseleonardo.lojavirtual.model.Produto;
import com.joseleonardo.lojavirtual.model.StatusRastreio;
import com.joseleonardo.lojavirtual.model.VendaCompraLojaVirtual;
import com.joseleonardo.lojavirtual.model.dto.ItemVendaLojaDTO;
import com.joseleonardo.lojavirtual.model.dto.VendaCompraLojaVirtualDTO;
import com.joseleonardo.lojavirtual.repository.EnderecoRepository;
import com.joseleonardo.lojavirtual.repository.NotaFiscalVendaRepository;
import com.joseleonardo.lojavirtual.repository.ProdutoRepository;
import com.joseleonardo.lojavirtual.repository.StatusRastreioRepository;
import com.joseleonardo.lojavirtual.repository.VendaCompraLojaVirtualRepository;
import com.joseleonardo.lojavirtual.service.VendaCompraLojaVirtualService;

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
	private StatusRastreioRepository statusRastreioRepository;
	
	@Autowired
	private VendaCompraLojaVirtualService vendaCompraLojaVirtualService;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@ResponseBody
	@PostMapping(value = "**/salvarVendaCompraLojaVirtual")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaCompraLojaVirtual(
	        @RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) 
					throws LojaVirtualException { 
		vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		PessoaFisica pessoaFisica = pessoaFisicaController.salvarPessoaFisica(vendaCompraLojaVirtual.getPessoa()).getBody();
		vendaCompraLojaVirtual.setPessoa(pessoaFisica);
		
		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);
		
		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
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
		
		StatusRastreio statusRastreio = new StatusRastreio();
		statusRastreio.setCentroDistribuicao("Loja local");
		statusRastreio.setCidade("Local");
		statusRastreio.setEstado("Local");
		statusRastreio.setStatus("Inicio compra");
		statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		statusRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		
		statusRastreioRepository.save(statusRastreio);
		
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
		VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
		        .buscarVendaCompraLojaVirtualPorIdSemExclusao(id).orElse(null);
		
		if (vendaCompraLojaVirtual == null) {
			throw new LojaVirtualException("Não econtrou nenhuma venda com o código: " + id);
		}

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
	
}
