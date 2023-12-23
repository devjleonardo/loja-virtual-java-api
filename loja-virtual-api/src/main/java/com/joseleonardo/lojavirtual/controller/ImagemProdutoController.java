package com.joseleonardo.lojavirtual.controller;

import java.util.ArrayList;
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

import com.joseleonardo.lojavirtual.model.ImagemProduto;
import com.joseleonardo.lojavirtual.model.dto.ImagemProdutoDTO;
import com.joseleonardo.lojavirtual.repository.ImagemProdutoRepository;
import com.joseleonardo.lojavirtual.repository.ProdutoRepository;

@RestController
public class ImagemProdutoController {

	@Autowired
	private ImagemProdutoRepository imagemProdutoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@ResponseBody
	@PostMapping(value = "**/salvarImagemProduto")
	public ResponseEntity<ImagemProdutoDTO> salvarImagemProduto(
			@RequestBody ImagemProduto imagemProduto) {
		imagemProduto = imagemProdutoRepository.saveAndFlush(imagemProduto);
		
		ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
		imagemProdutoDTO.setId(imagemProduto.getId());
		imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
		imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
		imagemProdutoDTO.setProdutoId(imagemProduto.getProduto().getId());
		imagemProdutoDTO.setEmpresaId(imagemProduto.getEmpresa().getId());
		
		return new ResponseEntity<ImagemProdutoDTO>(imagemProdutoDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deletarImagemProdutoPorId/{id}")
	public ResponseEntity<?> deletarImagemProdutoPorId(@PathVariable("id") Long id) {
		if (!imagemProdutoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("A imagem de código "
					+ id + " já foi removida ou não existe", HttpStatus.OK);
		}
		
		imagemProdutoRepository.deleteById(id);

		return new ResponseEntity<>("Imagem removida", HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deletarTodasImagemProdutoPorProdutoId/{produtoId}")
	public ResponseEntity<?> deletarTodasImagemProdutoPorProdutoId(
			@PathVariable("produtoId") Long produtoId) {
		if (!produtoRepository.findById(produtoId).isPresent()) {
			return new ResponseEntity<>("O produto de código "+ produtoId 
					+ " não existe", HttpStatus.OK);
		}
		
		imagemProdutoRepository.deletarTodasImagemProdutoPorProdutoId(produtoId);

		return new ResponseEntity<>("Imagens removida", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarImagemProdutoPorProdutoId/{produtoId}")
	public ResponseEntity<List<ImagemProdutoDTO>> buscarImagemProdutoPorProdutoId(
			@PathVariable("produtoId") Long produtoId){
		List<ImagemProdutoDTO> imagensProdutoDTO = new ArrayList<>();
		
		List<ImagemProduto> imagensProduto = imagemProdutoRepository
				.buscarImagemProdutoPorProdutoId(produtoId);
		
		for (ImagemProduto imagemProduto : imagensProduto) {
			ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
			imagemProdutoDTO.setId(imagemProduto.getId());
			imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
			imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
			imagemProdutoDTO.setProdutoId(imagemProduto.getProduto().getId());
			imagemProdutoDTO.setEmpresaId(imagemProduto.getEmpresa().getId());
			
			imagensProdutoDTO.add(imagemProdutoDTO);
		}
	
		return new ResponseEntity<List<ImagemProdutoDTO>>(imagensProdutoDTO, HttpStatus.OK);
	}
	
}
