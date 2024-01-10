package com.joseleonardo.lojavirtual.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.FormaPagamento;
import com.joseleonardo.lojavirtual.repository.FormaPagamentoRepository;
import com.joseleonardo.lojavirtual.repository.PessoaJuridicaRepository;

@RestController
public class FormaPagamentoController {

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@ResponseBody
	@PostMapping(value = "**/salvarFormaPagamento")
	public ResponseEntity<FormaPagamento> salvarFormaPagamento(
			@RequestBody @Valid FormaPagamento formaPagamento) throws LojaVirtualException { 
		formaPagamento = formaPagamentoRepository.save(formaPagamento);
		
		return new ResponseEntity<FormaPagamento>(formaPagamento, HttpStatus.CREATED);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarFormaPagamentoPorEmpresaId/{empresaId}")
	public ResponseEntity<List<FormaPagamento>> buscarFormaPagamentoPorEmpresaId(
			@PathVariable("empresaId") Long empresaId) throws LojaVirtualException {
		if (!pessoaJuridicaRepository.findById(empresaId).isPresent()) {
			throw new LojaVirtualException("Não econtrou nenhuma empresa com o código: " 
		            + empresaId);
		}
		
		List<FormaPagamento> formasDePagamentoDaEmpresa = formaPagamentoRepository
				.buscarFormaPagamentoPorEmpresaId(empresaId);
		
		return new ResponseEntity<List<FormaPagamento>>(formasDePagamentoDaEmpresa, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/listarTodasFormasDePagamento")
	public ResponseEntity<List<FormaPagamento>> listarTodasFormasDePagamento() {
		List<FormaPagamento> todasFormasDePagamento = formaPagamentoRepository.findAll();
		
		return new ResponseEntity<List<FormaPagamento>>(todasFormasDePagamento, HttpStatus.OK);
	}
	
}
