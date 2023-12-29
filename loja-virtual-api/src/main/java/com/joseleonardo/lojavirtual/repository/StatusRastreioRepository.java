package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.StatusRastreio;

@Repository
@Transactional
public interface StatusRastreioRepository extends JpaRepository<StatusRastreio, Long> {

	@Query("SELECT sr FROM StatusRastreio sr WHERE sr.vendaCompraLojaVirtual.id = ?1 "
		 + "ORDER BY sr.id")
	List<StatusRastreio> buscarStatusRastreioPorVendaId(Long vendaId);
	
}
