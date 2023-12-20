package com.joseleonardo.lojavirtual.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.PessoaFisica;
import com.joseleonardo.lojavirtual.repository.PessoaFisicaRepository;
import com.joseleonardo.lojavirtual.service.PessoaFisicaService;
import com.joseleonardo.lojavirtual.util.ValidacaoCPF;

@RestController
public class PessoaFisicaController {

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;
	
	@ResponseBody
	@PostMapping(value = "**/salvarPessoaFisica")
	public ResponseEntity<PessoaFisica> salvarPessoaFisica(
			@RequestBody @Valid PessoaFisica pessoaFisica) throws LojaVirtualException {
		if (pessoaFisica == null) {
			throw new LojaVirtualException("Pessoa física não pode ser nula");
		}
		
		String cpfSemMascara = pessoaFisica.getCpf().replaceAll("\\.", "").replaceAll("\\-", "");

		pessoaFisica.setCpf(cpfSemMascara);

		if (pessoaFisica.getId() == null &&
				pessoaFisicaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
			throw new LojaVirtualException("Não foi possível cadastrar, pois "
					+ "já existe uma pessoa jurídica cadastrada com o CPF " + pessoaFisica.getCpf());
		}

		if (!ValidacaoCPF.isCPF(pessoaFisica.getCpf())) {
			throw new LojaVirtualException("O CPF " + pessoaFisica.getCpf() + " está inválido");
		}

		pessoaFisica = pessoaFisicaService.salvar(pessoaFisica);

		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}
	
}
