package com.joseleonardo.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.lojavirtual.model.Acesso;
import com.joseleonardo.lojavirtual.repository.AcessoRepository;
import com.joseleonardo.lojavirtual.service.AcessoService;

@RestController
public class AcessoController {

	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@ResponseBody /* Pode dar um retorno da API */
	@PostMapping(value = "**/salvarAcesso") /* Mapeando a URL para receber JSON */
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) { /* Recebe o JSON e converte pra Objeto */
		Acesso acessoSalvo = acessoService.salvar(acesso);
		
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.CREATED);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deletarAcessoPorId/{id}")
	public ResponseEntity<?> deletarAcessoPorId(@PathVariable("id") Long id) {
		acessoRepository.deleteById(id);
		
		return new ResponseEntity<>("Aceso removido", HttpStatus.OK);
	}
	
}
