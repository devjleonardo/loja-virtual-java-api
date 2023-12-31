package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.FormaPagamento;

@Repository
@Transactional
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

	@Query("SELECT fp FROM FormaPagamento fp WHERE fp.empresa.id = ?1")
	List<FormaPagamento> buscarFormaPagamentoPorEmpresaId(Long empresaId);
	
}
