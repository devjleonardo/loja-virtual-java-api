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
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody @Valid Acesso acesso) 
			throws LojaVirtualException { 
		if (acesso.getId() == null) {
			List<Acesso> acessosExistente = acessoRepository
					.buscarAcessoPorNome(acesso.getNome().trim().toUpperCase());
		
			if (!acessosExistente.isEmpty()) {
				throw new LojaVirtualException("Não foi possível cadastrar, pois "
						+ "já existe um acesso cadastrado com o nome " + acesso.getNome());
			}
		}
		
		Acesso acessoSalvo = acessoService.salvar(acesso);
		
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.CREATED);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deletarAcessoPorId/{id}")
	public ResponseEntity<?> deletarAcessoPorId(@PathVariable("id") Long id) {
		if (!acessoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("O acesso de código "
					+ id + " já foi removido ou não existe", HttpStatus.OK);
		}
		
		acessoRepository.deleteById(id);
		
		return new ResponseEntity<>("Acesso removido", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarAcessoPorId/{id}")
	public ResponseEntity<Acesso> buscarAcessoPorId(@PathVariable("id") Long id) 
			throws LojaVirtualException {
		Acesso acesso = acessoRepository.findById(id).orElse(null);
		
		if (acesso == null) {
			throw new LojaVirtualException("Não econtrou nenhum acesso com o código " + id);
		}

		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscarAcessoPorNome/{nome}")
	public ResponseEntity<List<Acesso>> buscarAcessoPorNome(@PathVariable("nome") String nome) {
		List<Acesso> acessos = acessoRepository.buscarAcessoPorNome(nome.trim().toUpperCase());

		return new ResponseEntity<List<Acesso>>(acessos, HttpStatus.OK);
	}
	
}
