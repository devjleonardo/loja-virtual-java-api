package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.joseleonardo.lojavirtual.model.PessoaJuridica;

@Repository
public interface PessoaJuridicaRepository extends CrudRepository<PessoaJuridica, Long> {

	@Query("SELECT pj FROM PessoaJuridica pj WHERE UPPER(TRIM(pj.nome)) LIKE %?1%")
	List<PessoaJuridica> buscarPessoaJuridicaPorNome(String nome);
	
	@Query("SELECT pj FROM PessoaJuridica pj WHERE pj.cnpj = ?1")
	PessoaJuridica existeCnpjCadastrado(String cnpj);
	
	@Query("SELECT pj FROM PessoaJuridica pj WHERE pj.inscricaoEstadual = ?1")
	PessoaJuridica existeInscricaoEstadualCadastrada(String inscricaoEstadual);

	@Query("SELECT pj FROM PessoaJuridica pj WHERE pj.cnpj = ?1")
	PessoaJuridica buscarPessoaJuridicaPorCnpj(String cnpj);
	
}
