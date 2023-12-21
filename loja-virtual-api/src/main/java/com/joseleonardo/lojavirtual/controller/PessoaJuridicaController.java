package com.joseleonardo.lojavirtual.controller;

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
import com.joseleonardo.lojavirtual.model.Endereco;
import com.joseleonardo.lojavirtual.model.PessoaJuridica;
import com.joseleonardo.lojavirtual.model.dto.CepDTO;
import com.joseleonardo.lojavirtual.repository.EnderecoRepository;
import com.joseleonardo.lojavirtual.repository.PessoaJuridicaRepository;
import com.joseleonardo.lojavirtual.service.ConsultaCepService;
import com.joseleonardo.lojavirtual.service.PessoaJuridicaService;
import com.joseleonardo.lojavirtual.util.ValidacaoCNPJ;

@RestController
public class PessoaJuridicaController {
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Autowired
	private PessoaJuridicaService peossaJuridicaService;
	
	@Autowired
	private ConsultaCepService consultaCepService;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarPessoaJuridica")
	public ResponseEntity<PessoaJuridica> salvarPessoaJuridica(
			@RequestBody @Valid PessoaJuridica pessoaJuridica) throws LojaVirtualException {
		if (pessoaJuridica == null) {
			throw new LojaVirtualException("Pessoa jurídica não pode ser nula");
		}
		
		String cnpjSemMascara = pessoaJuridica.getCnpj()
				.replaceAll("\\.", "").replaceAll("\\/", "").replaceAll("\\-", "");
		
		pessoaJuridica.setCnpj(cnpjSemMascara);

		if (pessoaJuridica.getId() == null 
				&& pessoaJuridicaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			throw new LojaVirtualException("Não foi possível cadastrar, pois "
					+ "já existe uma pessoa jurídica cadastrada com o CNPJ "
					+ pessoaJuridica.getCnpj());
		}
		
		if (pessoaJuridica.getId() == null &&
				pessoaJuridicaRepository.existeInscricaoEstadualCadastrada(
						pessoaJuridica.getInscricaoEstadual()) != null) {
			throw new LojaVirtualException("Não foi possível cadastrar, pois "
			        + "já existe uma pessoa jurídica cadastrada com a inscrição estadual " 
			        + pessoaJuridica.getInscricaoEstadual());
		}
		
		if (!ValidacaoCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
			throw new LojaVirtualException("O CNPJ " + pessoaJuridica.getCnpj() + " está inválido");
		}
		
		if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {
			for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++) {
				CepDTO cepDTO = consultaCepService
						.consultarCep(pessoaJuridica.getEnderecos().get(i).getCep());

				pessoaJuridica.getEnderecos().get(i).setLogradouro(cepDTO.getLogradouro());
				pessoaJuridica.getEnderecos().get(i).setBairro(cepDTO.getBairro());
				pessoaJuridica.getEnderecos().get(i).setComplemento(cepDTO.getComplemento());
				pessoaJuridica.getEnderecos().get(i).setCep(cepDTO.getLocalidade());
				pessoaJuridica.getEnderecos().get(i).setUf(cepDTO.getUf());
			}
		} else {
			for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++) {
				Endereco enderecoAtual = enderecoRepository.findById(
						pessoaJuridica.getEnderecos().get(i).getId()).get();

				if (!enderecoAtual.getCep().equals(pessoaJuridica.getEnderecos().get(i).getCep())) {
					CepDTO cepDTO = consultaCepService
							.consultarCep(pessoaJuridica.getEnderecos().get(i).getCep());

					pessoaJuridica.getEnderecos().get(i).setLogradouro(cepDTO.getLogradouro());
					pessoaJuridica.getEnderecos().get(i).setBairro(cepDTO.getBairro());
					pessoaJuridica.getEnderecos().get(i).setComplemento(cepDTO.getComplemento());
					pessoaJuridica.getEnderecos().get(i).setCep(cepDTO.getLocalidade());
					pessoaJuridica.getEnderecos().get(i).setUf(cepDTO.getUf());
				}
			}
		}

		pessoaJuridica = peossaJuridicaService.salvar(pessoaJuridica);

		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarPessoaJuridicaPorNome/{nome}")
	public ResponseEntity<List<PessoaJuridica>> buscarPessoaFisicaPorNome(
			@PathVariable("nome") String nome) {
		List<PessoaJuridica> pessoasJuridicas = pessoaJuridicaRepository
				.buscarPessoaJuridicaPorNome(nome.trim().toUpperCase());

		return new ResponseEntity<List<PessoaJuridica>>(pessoasJuridicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarPessoaJuridicaPorCnpj/{cnpj}")
	public ResponseEntity<PessoaJuridica> buscarPessoaJuridicaPorCnpj(
			@PathVariable("cnpj") String cnpj) throws LojaVirtualException {
		PessoaJuridica pessoaFisica = pessoaJuridicaRepository.buscarPessoaJuridicaPorCnpj(cnpj);
		
		if (pessoaFisica == null) {
			throw new LojaVirtualException("Não existe nenhuma pessoa jurídica cadastrada "
					+ "com esse CNPJ " + cnpj);
		}

		return new ResponseEntity<PessoaJuridica>(pessoaFisica, HttpStatus.OK);
	}

}
