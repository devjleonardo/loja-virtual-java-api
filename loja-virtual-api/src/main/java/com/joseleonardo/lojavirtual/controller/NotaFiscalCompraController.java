package com.joseleonardo.lojavirtual.controller;

import java.util.ArrayList;
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
import com.joseleonardo.lojavirtual.model.NotaFiscalCompra;
import com.joseleonardo.lojavirtual.model.dto.RelatorioNotaFiscalCompraProdutosCompradosDTO;
import com.joseleonardo.lojavirtual.repository.NotaFiscalCompraRepository;
import com.joseleonardo.lojavirtual.repository.NotaItemProdutoRepository;
import com.joseleonardo.lojavirtual.service.NotaFiscalCompraService;

@RestController
public class NotaFiscalCompraController {

	@Autowired
	private NotaFiscalCompraRepository notaFiscalCompraRepository;

	@Autowired
	private NotaItemProdutoRepository notaItemProdutoRepository;
	
	@Autowired
	private NotaFiscalCompraService notaFiscalCompraService;

	@ResponseBody
	@PostMapping(value = "**/salvarNotaFiscalCompra")
	public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(
			@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws LojaVirtualException {
		if (notaFiscalCompra.getId() == null) {
			if (notaFiscalCompra.getDescricaoObservacao() != null) {
				List<NotaFiscalCompra> marcasProduto = notaFiscalCompraRepository
						.buscarNotaFiscalCompraPorDescricaoObservacao(
								notaFiscalCompra.getDescricaoObservacao().trim().toUpperCase());

				if (!marcasProduto.isEmpty()) {
					throw new LojaVirtualException("Não foi possível cadastrar, pois "
							+ "já existe uma nota fiscal de com a descrição e observação: " 
							+ notaFiscalCompra.getDescricaoObservacao());
				}
			}
		}

		if(notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0) {
			throw new LojaVirtualException("A pessoa da nota fiscal de compra "
					+ "deve ser informada");
		}

		if (notaFiscalCompra.getContaPagar() == null 
				|| notaFiscalCompra.getContaPagar().getId() <= 0) {
			throw new LojaVirtualException("A conta a pagar da nota fiscal de compra "
					+ "deve ser informada");
		}

		if (notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0) {
			throw new LojaVirtualException("A empresa da nota fiscal de compra "
					+ "deve ser informada");
		}

		NotaFiscalCompra notaFiscalCompraSalva = notaFiscalCompraRepository.save(notaFiscalCompra);

		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompraSalva, HttpStatus.CREATED);
	}

	@ResponseBody
	@DeleteMapping(value = "**/deletarNotaFiscalCompraPorId/{id}")
	public ResponseEntity<?> deletarMarcaProdutoPorId(@PathVariable("id") Long id) {
		if (!notaFiscalCompraRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("A nota fiscal de compra de código "
					+ id + " já foi removida ou não existe", HttpStatus.OK);
		}

		/* Deleta o filho */
		notaItemProdutoRepository.deletarNotaItemProdutoPorNotaFiscalCompraId(id);

		/* Delta o pai */
		notaFiscalCompraRepository.deleteById(id);

		return new ResponseEntity<>("Nota fiscal de compra removida", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscarNotaFiscalCompraPorId/{id}")
	public ResponseEntity<NotaFiscalCompra> obterNotaFiscalCompraPorId(@PathVariable("id") Long id) 
			throws LojaVirtualException {
		NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

		if (notaFiscalCompra == null) {
			throw new LojaVirtualException("Não econtrou nenhuma nota fiscal de compra "
					+ "com o código: " + id);
		}

		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscarNotaFiscalCompraPorDescricaoObservacao/{descricaoObservacao}")
	public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalCompraPorNome(
			@PathVariable("descricaoObservacao") String descricaoObservacao) {
		List<NotaFiscalCompra> notasFiscaisCompra = notaFiscalCompraRepository
				.buscarNotaFiscalCompraPorDescricaoObservacao(
						descricaoObservacao.trim().toUpperCase());

		return new ResponseEntity<List<NotaFiscalCompra>>(notasFiscaisCompra, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "**/relatorioNotaFiscalCompraProdutosComprados")
	public ResponseEntity<List<RelatorioNotaFiscalCompraProdutosCompradosDTO>> relatorioNotaFiscalCompraProdutosComprados(
			@Valid @RequestBody RelatorioNotaFiscalCompraProdutosCompradosDTO relatorioNotaFiscalCompraProdutosCompradosDTO) {
		List<RelatorioNotaFiscalCompraProdutosCompradosDTO> retorno = new ArrayList<>();
		
		retorno = notaFiscalCompraService
				.gerarRelatorioProdutosComprados(relatorioNotaFiscalCompraProdutosCompradosDTO);
		
		return new ResponseEntity<List<RelatorioNotaFiscalCompraProdutosCompradosDTO>>(
				retorno, HttpStatus.OK);
	}

}
