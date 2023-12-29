package com.joseleonardo.lojavirtual.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
