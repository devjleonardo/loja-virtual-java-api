package com.joseleonardo.lojavirtual.controller;

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
import com.joseleonardo.lojavirtual.model.Produto;
import com.joseleonardo.lojavirtual.repository.ProdutoRepository;

@RestController
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarProduto")
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) 
			throws LojaVirtualException {

		if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
			throw new LojaVirtualException("A empresa do produto deve ser informada");
		}

		if (produto.getId() == null) {
			List<Produto> produtos = produtoRepository
					.buscarProdutoPorNomeEPorEmpresaId(
							produto.getNome().toUpperCase(), produto.getEmpresa().getId());

			if (!produtos.isEmpty()) {
				throw new LojaVirtualException("Não foi possível cadastrar, pois "
						+ "já existe um produto com o nome: " + produto.getNome());
			}
		}

		if (produto.getCategoriaProduto() == null || produto.getEmpresa().getId() <= 0) {
			throw new LojaVirtualException("A categoria do produto deve ser informada");
		}

		if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
			throw new LojaVirtualException("A marca do produto deve ser informada");
		}

		Produto produtoSalvo = produtoRepository.save(produto);

		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.CREATED);
	}

	@ResponseBody
	@DeleteMapping(value = "**/deletarProdutoPorId/{id}")
	public ResponseEntity<?> deletarProdutoPorId(@PathVariable("id") Long id) {
		if (!produtoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("O produto de código "
					+ id + " já foi removido ou não existe", HttpStatus.OK);
		}
		
		produtoRepository.deleteById(id);

		return new ResponseEntity<>("Produto removido", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscarProdutoPorId/{id}")
	public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable("id") Long id) 
			throws LojaVirtualException {
		Produto produto = produtoRepository.findById(id).orElse(null);

		if (produto == null) {
			throw new LojaVirtualException("Não econtrou nenhum produto com o código: " + id);
		}
		
		return new ResponseEntity<Produto>(produto, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscarProdutoPorNome/{nome}")
	public ResponseEntity<List<Produto>> buscarProdutoPorNome(@PathVariable("nome") String nome) {
		List<Produto> produtos = produtoRepository.buscarProdutoPorNome(nome.trim().toUpperCase());

		return new ResponseEntity<List<Produto>>(produtos, HttpStatus.OK);
	}

}
