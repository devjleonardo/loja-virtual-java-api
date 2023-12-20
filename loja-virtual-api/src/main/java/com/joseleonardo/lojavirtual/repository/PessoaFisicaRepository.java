package com.joseleonardo.lojavirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.joseleonardo.lojavirtual.model.PessoaFisica;

@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {

	@Query("SELECT pf FROM PessoaFisica pf WHERE pf.cpf = ?1")
	PessoaFisica existeCpfCadastrado(String cpf);
	
}
