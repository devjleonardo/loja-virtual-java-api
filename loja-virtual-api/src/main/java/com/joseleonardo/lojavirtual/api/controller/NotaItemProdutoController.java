package com.joseleonardo.lojavirtual.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.NotaItemProduto;
import com.joseleonardo.lojavirtual.repository.NotaItemProdutoRepository;

@RestController
public class NotaItemProdutoController {

	@Autowired
	private NotaItemProdutoRepository notaItemProdutoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarNotaItemProduto") 
	public ResponseEntity<NotaItemProduto> salvarNotaItemProduto(
			@RequestBody @Valid NotaItemProduto notaItemProduto) throws LojaVirtualException {
		if (notaItemProduto.getId() == null) {
			if (notaItemProduto.getNotaFiscalCompra() == null 
					|| notaItemProduto.getNotaFiscalCompra().getId() <= 0) {
				throw new LojaVirtualException("A nota fiscal de compra da nota de item produto "
						+ "deve ser informada");
			}

			if (notaItemProduto.getProduto() == null || notaItemProduto.getProduto().getId() <= 0) {
				throw new LojaVirtualException("O produto da nota de item produto deve "
						+ "ser informado");
			}

			if (notaItemProduto.getEmpresa() == null || notaItemProduto.getEmpresa().getId() <= 0) {
				throw new LojaVirtualException("A empresa da nota de item produto deve "
						+ "ser informada");
			}

			List<NotaItemProduto> notaItemProdutoExiste = notaItemProdutoRepository
					.buscarNotaItemProdutoPorNotaFiscalCompraIdEProdutoId(
							notaItemProduto.getNotaFiscalCompra().getId(), 
							notaItemProduto.getProduto().getId());

			if (!notaItemProdutoExiste.isEmpty()) {
				throw new LojaVirtualException("Não foi possível cadastrar, pois "
						+ "já existe uma nota de item produto para a "
						+ "nota fiscal de compra de código " 
						+ notaItemProduto.getNotaFiscalCompra().getId()
						+ " e produto de codigo " + notaItemProduto.getProduto().getId());
			}
		}

		NotaItemProduto notaItemProdutoSalva = notaItemProdutoRepository.save(notaItemProduto);

		notaItemProdutoSalva = notaItemProdutoRepository.findById(notaItemProdutoSalva.getId()).get();

		return new ResponseEntity<NotaItemProduto>(notaItemProdutoSalva, HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "**/deletarNotaItemProdutoPorId/{id}")
	public ResponseEntity<?> deletarNotaItemProdutoPorId(@PathVariable("id") Long id) {
		if (!notaItemProdutoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("A nota de item produto de código "
					+ id + " já foi removida ou não existe", HttpStatus.OK);
		}

		notaItemProdutoRepository.deleteById(id);

		return new ResponseEntity<>("Nota de item produto removida", HttpStatus.OK);
	}
	
}
