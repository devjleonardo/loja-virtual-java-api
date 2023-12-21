package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.joseleonardo.lojavirtual.model.PessoaFisica;

@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {
	
	@Query("SELECT pf FROM PessoaFisica pf WHERE UPPER(TRIM(pf.nome)) LIKE %?1%")
	List<PessoaFisica> buscarPessoaFisicaPorNome(String nome);

	@Query("SELECT pf FROM PessoaFisica pf WHERE pf.cpf = ?1")
	PessoaFisica existeCpfCadastrado(String cpf);
	
	@Query("SELECT pf FROM PessoaFisica pf WHERE pf.cpf = ?1")
	PessoaFisica buscarPessoaFisicaPorCpf(String cpf);
	
}
