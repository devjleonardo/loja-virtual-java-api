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
import com.joseleonardo.lojavirtual.model.PessoaFisica;
import com.joseleonardo.lojavirtual.model.enums.TipoPessoa;
import com.joseleonardo.lojavirtual.repository.PessoaFisicaRepository;
import com.joseleonardo.lojavirtual.service.AtualizacaoQuantidadeAcessoEndPointService;
import com.joseleonardo.lojavirtual.service.PessoaFisicaService;
import com.joseleonardo.lojavirtual.util.ValidacaoCPF;

@RestController
public class PessoaFisicaController {

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;
	
	@Autowired
	private AtualizacaoQuantidadeAcessoEndPointService atualizacaoQuantidadeAcessoEndPointService;
	
	@ResponseBody
	@PostMapping(value = "**/salvarPessoaFisica")
	public ResponseEntity<PessoaFisica> salvarPessoaFisica(
			@RequestBody @Valid PessoaFisica pessoaFisica) throws LojaVirtualException {
		if (pessoaFisica == null) {
			throw new LojaVirtualException("Pessoa física não pode ser nula");
		}
		
		if (pessoaFisica.getTipoPessoa() == null) {
			pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name());
		}
		
		String cpfSemMascara = pessoaFisica.getCpf().replaceAll("\\.", "").replaceAll("\\-", "");

		pessoaFisica.setCpf(cpfSemMascara);

		if (pessoaFisica.getId() == null &&
				pessoaFisicaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
			throw new LojaVirtualException("Não foi possível cadastrar, pois "
					+ "já existe uma pessoa física cadastrada com o CPF: " + pessoaFisica.getCpf());
		}

		if (!ValidacaoCPF.isCPF(pessoaFisica.getCpf())) {
			throw new LojaVirtualException("O CPF " + pessoaFisica.getCpf() + " está inválido");
		}

		pessoaFisica = pessoaFisicaService.salvar(pessoaFisica);

		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarPessoaFisicaPorNome/{nome}")
	public ResponseEntity<List<PessoaFisica>> buscarPessoaFisicaPorNome(
			@PathVariable("nome") String nome) {
		List<PessoaFisica> pessoasFisicas = pessoaFisicaRepository
				.buscarPessoaFisicaPorNome(nome.trim().toUpperCase());

		atualizacaoQuantidadeAcessoEndPointService.buscarPessoaFisicaPorNome();
		
		return new ResponseEntity<List<PessoaFisica>>(pessoasFisicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarPessoaFisicaPorCpf/{cpf}")
	public ResponseEntity<PessoaFisica> buscarPessoaFisicaPorCpf(@PathVariable("cpf") String cpf) 
			throws LojaVirtualException {
		PessoaFisica pessoaFisica = pessoaFisicaRepository.buscarPessoaFisicaPorCpf(cpf);
		
		if (pessoaFisica == null) {
			throw new LojaVirtualException("Não existe nenhuma pessoa física cadastrada "
					+ "com esse CPF " + cpf);
		}

		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}
	
}
