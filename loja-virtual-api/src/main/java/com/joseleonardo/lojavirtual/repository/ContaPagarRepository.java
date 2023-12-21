package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.ContaPagar;

@Repository
@Transactional
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long>{

	@Query("SELECT cp FROM ContaPagar cp WHERE UPPER(TRIM(cp.descricao)) LIKE %?1%")
	List<ContaPagar> buscarContaPagarPorDescricao(String descricao);

	@Query("SELECT cp FROM ContaPagar cp WHERE cp.pessoaPagadora.id = ?1")
	List<ContaPagar> buscarContaPagarPorPessoaPagadoraId(Long pessoaPagadoraId);

	@Query("SELECT cp FROM ContaPagar cp WHERE cp.pessoaFornecedora.id = ?1")
	List<ContaPagar> buscarContaPagarPorPessoaFornecedoraId(Long pessoaFornecedoraId);

	@Query("SELECT cp FROM ContaPagar cp WHERE cp.empresa.id = ?1")
	List<ContaPagar> buscarContaPagarPorEmpresaId(Long empresaId);

}
