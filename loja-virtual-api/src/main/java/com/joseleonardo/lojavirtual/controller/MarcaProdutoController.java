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
import com.joseleonardo.lojavirtual.model.MarcaProduto;
import com.joseleonardo.lojavirtual.repository.MarcaProdutoRepository;

@RestController
public class MarcaProdutoController {

	@Autowired
	private MarcaProdutoRepository marcaProdutoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarMarcaProduto")
	public ResponseEntity<MarcaProduto> salvarMarcaProduto(
			@RequestBody @Valid MarcaProduto marcaProduto) throws LojaVirtualException {
		if (marcaProduto.getId() == null) {
			List<MarcaProduto> marcasProduto = marcaProdutoRepository
					.buscarMarcaProdutoPorNome(marcaProduto.getNome().toUpperCase());

			if (!marcasProduto.isEmpty()) {
				throw new LojaVirtualException("Não foi possível cadastrar, pois "
						+ "já existe uma marca de produto com o nome: " + marcaProduto.getNome());
			}
		}

		MarcaProduto marcaProdutoSalva = marcaProdutoRepository.save(marcaProduto);

		return new ResponseEntity<MarcaProduto>(marcaProdutoSalva, HttpStatus.CREATED);
	}

	@ResponseBody
	@DeleteMapping(value = "**/deletarMarcaProdutoPorId/{id}")
	public ResponseEntity<?> deletarMarcaProdutoPorId(@PathVariable("id") Long id) {
		if (!marcaProdutoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("A marca de produto de código "
					+ id + " já foi removida ou não existe", HttpStatus.OK);
		}

		marcaProdutoRepository.deleteById(id);

		return new ResponseEntity<>("Marca de produto removida", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscarMarcaProdutoPorId/{id}")
	public ResponseEntity<MarcaProduto> buscarMarcaProdutoPorId(@PathVariable("id") Long id) 
			throws LojaVirtualException {
		MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).orElse(null);

		if (marcaProduto == null) {
			throw new LojaVirtualException("Não econtrou nenhuma marca de produto com o código: " 
		            + id);
		}

		return new ResponseEntity<MarcaProduto>(marcaProduto, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscarMarcaProdutoPorNome/{nome}")
	public ResponseEntity<List<MarcaProduto>> buscarMarcaProdutoPorNome(@PathVariable("nome") String nome) {
		List<MarcaProduto> marcasProduto = marcaProdutoRepository
				.buscarMarcaProdutoPorNome(nome.trim().toUpperCase());

		return new ResponseEntity<List<MarcaProduto>>(marcasProduto, HttpStatus.OK);
	}

}
