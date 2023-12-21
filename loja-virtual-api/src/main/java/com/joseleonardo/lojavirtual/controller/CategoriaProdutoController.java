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
import com.joseleonardo.lojavirtual.model.CategoriaProduto;
import com.joseleonardo.lojavirtual.model.dto.CategoriaProdutoDTO;
import com.joseleonardo.lojavirtual.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {

	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarCategoriaProduto")
	public ResponseEntity<CategoriaProdutoDTO> salvarCategoriaProduto(
			@RequestBody CategoriaProduto categoriaProduto) throws LojaVirtualException {
		if (categoriaProduto.getEmpresa() == null 
				|| categoriaProduto.getEmpresa().getId() == null) {
			throw new LojaVirtualException("A empresa deve ser informada");
		}

		if (categoriaProduto.getId() == null 
				&& categoriaProdutoRepository.existeCategoriaProdutoCadastradaComNome(
						categoriaProduto.getNome().trim().toUpperCase())) {
			throw new LojaVirtualException("Não foi possível cadastrar, pois "
					+ "já existe uma categoria de produto com o nome " 
					+ categoriaProduto.getNome());
		}

		CategoriaProduto categoriaProdutoSalva = categoriaProdutoRepository.save(categoriaProduto);

		CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
		categoriaProdutoDTO.setId(categoriaProdutoSalva.getId());
		categoriaProdutoDTO.setNome(categoriaProdutoSalva.getNome());
		categoriaProdutoDTO.setEmpresa(categoriaProdutoSalva.getEmpresa().getId().toString());

		return new ResponseEntity<CategoriaProdutoDTO>(categoriaProdutoDTO, HttpStatus.OK);
	}

	@ResponseBody
	@DeleteMapping(value = "**/deletarCategoriaProdutoPorId/{id}")
	public ResponseEntity<?> deletarCategoriaProdutoPorId(@PathVariable("id") Long id) {
		if (!categoriaProdutoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("A categoria de produto de código "
					+ id + " já foi removida ou não existe", HttpStatus.OK);
		}
		
		categoriaProdutoRepository.deleteById(id);

		return new ResponseEntity<>("Categoria de produto removida", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/obterCategoriaProdutoPorId/{id}")
	public ResponseEntity<CategoriaProduto> obterCategoriaProdutoPorId(
			@PathVariable("id") Long id) throws LojaVirtualException {
		CategoriaProduto categoriaProduto = categoriaProdutoRepository.findById(id).orElse(null);

		if (categoriaProduto == null) {
			throw new LojaVirtualException("Não econtrou Categoria de produto com código: " + id);
		}

		return new ResponseEntity<CategoriaProduto>(categoriaProduto, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscarCategoriaProdutoPorNome/{nome}")
	public ResponseEntity<List<CategoriaProduto>> buscarCategoriaProdutoPorNome(
			@PathVariable("nome") String nome) {
		List<CategoriaProduto> categoriasProduto = categoriaProdutoRepository
				.buscarCategoriaProdutoPorNome(nome.trim().toUpperCase());

		return new ResponseEntity<List<CategoriaProduto>>(categoriasProduto, HttpStatus.OK);
	}

}
