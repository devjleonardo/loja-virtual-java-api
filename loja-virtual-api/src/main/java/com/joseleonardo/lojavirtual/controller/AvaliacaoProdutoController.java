package com.joseleonardo.lojavirtual.controller;

import java.util.List;

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
import com.joseleonardo.lojavirtual.model.AvaliacaoProduto;
import com.joseleonardo.lojavirtual.repository.AvaliacaoProdutoRepository;
import com.joseleonardo.lojavirtual.repository.PessoaFisicaRepository;
import com.joseleonardo.lojavirtual.repository.ProdutoRepository;

@RestController
public class AvaliacaoProdutoController {

	@Autowired
	private AvaliacaoProdutoRepository avaliacaoProdutoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@ResponseBody
	@PostMapping(value = "**/salvarAvaliacaoProduto")
	public ResponseEntity<AvaliacaoProduto> salvarAvaliacaoProduto(
			@RequestBody AvaliacaoProduto avaliacaoProduto) throws LojaVirtualException {
		if (avaliacaoProduto.getProduto() == null 
			    || (avaliacaoProduto.getProduto() != null 
				        && avaliacaoProduto.getProduto().getId() <= 0)) {
			throw new LojaVirtualException("O produto da avaliação do produto deve ser informado");
		}
		
		if (avaliacaoProduto.getPessoa() == null 
			    || (avaliacaoProduto.getPessoa() != null 
				        && avaliacaoProduto.getPessoa().getId() <= 0)) {
			throw new LojaVirtualException("A pessoa da avaliação do produto deve ser informada");
		}
		
		if (avaliacaoProduto.getEmpresa() == null 
			    || (avaliacaoProduto.getEmpresa() != null 
				        && avaliacaoProduto.getEmpresa().getId() <= 0)) {
			throw new LojaVirtualException("A empresa da avaliação do produto deve ser informada");
		}
		
		avaliacaoProduto = avaliacaoProdutoRepository.saveAndFlush(avaliacaoProduto);
		
		return new ResponseEntity<AvaliacaoProduto>(avaliacaoProduto, HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deletarAvaliacaoProdutoPorId/{id}")
	public ResponseEntity<?> deletarAvaliacaoProdutoPorId(@PathVariable("id") Long id) {
		if (!avaliacaoProdutoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("A Avaliação do produto de código "
					+ id + " já foi removida ou não existe", HttpStatus.OK);
		}
		
		avaliacaoProdutoRepository.deleteById(id);

		return new ResponseEntity<>("Avaliação do produto removida", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarAvaliacaoProdutoPorProdutoId/{produtoId}")
	public ResponseEntity<?> buscarAvaliacaoProdutoPorProdutoId(
			@PathVariable("produtoId") Long produtoId) {
		if (!produtoRepository.findById(produtoId).isPresent()) {
			return new ResponseEntity<>("O produto de código "+ produtoId 
					+ " não existe", HttpStatus.OK);
		}
		
		List<AvaliacaoProduto> avaliacoesProduto = avaliacaoProdutoRepository
				.buscarAvaliacaoProdutoPorProdutoId(produtoId);

		return new ResponseEntity<List<AvaliacaoProduto>>(avaliacoesProduto, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarAvaliacaoProdutoPorPessoaFisicaId/{pessoaId}")
	public ResponseEntity<?> buscarAvaliacaoProdutoPorPessoaFisicaId(
			@PathVariable("pessoaId") Long pessoaId) {
		if (!pessoaFisicaRepository.findById(pessoaId).isPresent()) {
			return new ResponseEntity<>("A pessoa física de código "+ pessoaId 
					+ " não existe", HttpStatus.OK);
		}
		
		List<AvaliacaoProduto> avaliacoesProduto = avaliacaoProdutoRepository
				.buscarAvaliacaoProdutoPorPessoaId(pessoaId);

		return new ResponseEntity<List<AvaliacaoProduto>>(avaliacoesProduto, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarAvaliacaoProdutoPorProdutoIdEPessoaFisicaId"
			          + "/{produtoId}/{pessoaFisicaId}")
	public ResponseEntity<?> buscarAvaliacaoProdutoPorPessoaFisicaId(
			@PathVariable("produtoId") Long produtoId, 
			@PathVariable("pessoaFisicaId") Long pessoaFisicaId) {
		if (!produtoRepository.findById(produtoId).isPresent()) {
			return new ResponseEntity<>("O produto de código "+ produtoId 
					+ " não existe", HttpStatus.OK);
		}
		
		if (!pessoaFisicaRepository.findById(pessoaFisicaId).isPresent()) {
			return new ResponseEntity<>("A pessoa física de código "+ pessoaFisicaId 
					+ " não existe", HttpStatus.OK);
		}
		
		List<AvaliacaoProduto> avaliacoesProduto = avaliacaoProdutoRepository
				.buscarAvaliacaoProdutoPorProdutoIdEPessoaId(produtoId, pessoaFisicaId);

		return new ResponseEntity<List<AvaliacaoProduto>>(avaliacoesProduto, HttpStatus.OK);
	}
	
}
