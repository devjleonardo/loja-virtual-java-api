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
import com.joseleonardo.lojavirtual.model.ContaPagar;
import com.joseleonardo.lojavirtual.repository.ContaPagarRepository;

@RestController
public class ContaPagarController {

	@Autowired
	private ContaPagarRepository contaPagarRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarContaPagar")
	public ResponseEntity<ContaPagar> salvarContaPagar(@RequestBody @Valid ContaPagar contaPagar) 
			throws LojaVirtualException {

		if (contaPagar.getPessoaPagadora() == null 
				|| contaPagar.getPessoaPagadora().getId() <= 0) {
			throw new LojaVirtualException("A pessoa pagadora da conta a pagar deve ser informada");
		}

		if (contaPagar.getPessoaFornecedora() == null 
				|| contaPagar.getPessoaFornecedora().getId() <= 0) {
			throw new LojaVirtualException("A pessoa fornecedora da conta a pagar "
					+ "deve ser informada");
		}

		if (contaPagar.getEmpresa() == null || contaPagar.getEmpresa().getId() <= 0) {
			throw new LojaVirtualException("A empresa da conta a pagar deve ser informada");
		}

		if (contaPagar.getId() == null) {
			List<ContaPagar> contasPagar = contaPagarRepository
					.buscarContaPagarPorDescricao(contaPagar.getDescricao().trim().toUpperCase());

			if (!contasPagar.isEmpty()) {
				throw new LojaVirtualException("Não foi possível cadastrar, pois "
						+ "já existe uma conta a pagar com a descrição: " 
						+ contaPagar.getDescricao());
			}
		}

		ContaPagar contaPagarSalva = contaPagarRepository.save(contaPagar);

		return new ResponseEntity<ContaPagar>(contaPagarSalva, HttpStatus.CREATED);
	}

	@ResponseBody
	@DeleteMapping(value = "**/deletarContaPagarPorId/{id}")
	public ResponseEntity<?> deletarContaPagarPorId(@PathVariable("id") Long id) {
		if (!contaPagarRepository.findById(id).isPresent()) {
			return new ResponseEntity<>("A conta a pagar de código "
					+ id + " já foi removida ou não existe", HttpStatus.OK);
		}

		contaPagarRepository.deleteById(id);

		return new ResponseEntity<>("Conta a pagar removida", HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscarContaPagarPorId/{id}")
	public ResponseEntity<ContaPagar> buscarContaPagarPorId(@PathVariable("id") Long id) 
			throws LojaVirtualException {
		ContaPagar contaPagar = contaPagarRepository.findById(id).orElse(null);

		if (contaPagar == null) {
			throw new LojaVirtualException("Não econtrou nenhuma conta a pagar com o código: " 
		    + id);
		}

		return new ResponseEntity<ContaPagar>(contaPagar, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/buscarContaPagarPorDescricao/{descricao}")
	public ResponseEntity<List<ContaPagar>> buscarContaPagarPorDescricao(
			@PathVariable("descricao") String descricao) {
		List<ContaPagar> contasPagar = contaPagarRepository
				.buscarContaPagarPorDescricao(descricao.trim().toUpperCase());

		return new ResponseEntity<List<ContaPagar>>(contasPagar, HttpStatus.OK);
	}

}
