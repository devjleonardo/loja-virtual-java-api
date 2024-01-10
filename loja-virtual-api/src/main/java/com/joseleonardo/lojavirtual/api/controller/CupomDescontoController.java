package com.joseleonardo.lojavirtual.api.controller;

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
import com.joseleonardo.lojavirtual.model.CupomDesconto;
import com.joseleonardo.lojavirtual.repository.CupomDescontoRepository;
import com.joseleonardo.lojavirtual.repository.PessoaJuridicaRepository;

@RestController
public class CupomDescontoController {

	@Autowired
	private CupomDescontoRepository cupomDescontoRepository;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@ResponseBody
	@PostMapping(value = "**/salvarCupomDesconto")
	public ResponseEntity<CupomDesconto> salvarCupomDesconto(
			@RequestBody @Valid CupomDesconto cupomDesconto) throws LojaVirtualException { 
		CupomDesconto cupomDescontoSalvo = cupomDescontoRepository.save(cupomDesconto);
		
		return new ResponseEntity<CupomDesconto>(cupomDescontoSalvo, HttpStatus.CREATED);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deletarCupomDescontoPorId/{id}")
	public ResponseEntity<?> deletarCupomDescontoPorId(@PathVariable("id") Long id) {
		if (!cupomDescontoRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("O cupom de desconto de código "
					+ id + " já foi removido ou não existe", HttpStatus.OK);
		}
		
		cupomDescontoRepository.deleteById(id);
		
		return new ResponseEntity<>("Cupom de desconto removido", HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarCupomDescontoPorId/{id}")
	public ResponseEntity<CupomDesconto> buscarCupomDescontoPorId(@PathVariable("id") Long id) 
			throws LojaVirtualException {
		CupomDesconto acesso = cupomDescontoRepository.findById(id).orElse(null);
		
		if (acesso == null) {
			throw new LojaVirtualException("Não econtrou nenhum cupom de desconto com o código: " 
		            + id);
		}

		return new ResponseEntity<CupomDesconto>(acesso, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarCupomDescontoPorEmpresaId/{empresaId}")
	public ResponseEntity<List<CupomDesconto>> buscarCupomDescontoPorEmpresaId(
			@PathVariable("empresaId") Long empresaId) throws LojaVirtualException {
		if (!pessoaJuridicaRepository.findById(empresaId).isPresent()) {
			throw new LojaVirtualException("Não econtrou nenhuma empresa com o código: " 
		            + empresaId);
		}
		
		List<CupomDesconto> cuponsDeDescontoDaEmpresa = cupomDescontoRepository
				.buscarCupomDescontoPorEmpresaId(empresaId);
		
		return new ResponseEntity<List<CupomDesconto>>(cuponsDeDescontoDaEmpresa, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/listarTodosCuponsDeDesconto")
	public ResponseEntity<List<CupomDesconto>> listarTodosCuponsDeDesconto() {
		List<CupomDesconto> todosCuponsDeDesconto = cupomDescontoRepository.findAll();
		
		return new ResponseEntity<List<CupomDesconto>>(todosCuponsDeDesconto, HttpStatus.OK);
	}
	
}
