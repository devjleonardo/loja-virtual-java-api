package com.joseleonardo.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.PessoaJuridica;
import com.joseleonardo.lojavirtual.repository.PessoaJuridicaRepository;
import com.joseleonardo.lojavirtual.service.PessoaJuridicaService;

@RestController
public class PessoaJuridicaController {
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;

	@Autowired
	private PessoaJuridicaService peossaJuridicaService;

	@ResponseBody
	@PostMapping(value = "**/salvarPessoaJuridica")
	public ResponseEntity<PessoaJuridica> salvar(@RequestBody PessoaJuridica pessoaJuridica) 
			throws LojaVirtualException {
		if (pessoaJuridica == null) {
			throw new LojaVirtualException("Pessoa jurídica não pode ser nula");
		}

		if (pessoaJuridica.getId() == null 
				&& pessoaJuridicaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			throw new LojaVirtualException("Já existe CNPJ cadastrado com o número " 
			       + pessoaJuridica.getCnpj());
		}

		pessoaJuridica = peossaJuridicaService.salvar(pessoaJuridica);

		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}

}
