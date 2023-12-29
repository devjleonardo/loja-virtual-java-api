package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.NotaFiscalVenda;

@Repository
@Transactional
public interface NotaFiscalVendaRepository extends JpaRepository<NotaFiscalVenda, Long> {

	@Query("SELECT nv FROM NotaFiscalVenda nv WHERE nv.vendaCompraLojaVirtual.id = ?1")
	List<NotaFiscalVenda> buscarNotaFiscalVendaPorVendaId(Long vendaId);
	
}
