package com.joseleonardo.lojavirtual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.AsaasStatusPagamentoCartaoCredito;

@Repository
@Transactional
public interface AsaasStatusPagamentoCartaoCreditoRepository 
		extends JpaRepository<AsaasStatusPagamentoCartaoCredito, Long> {

	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(nativeQuery = true,
	       value = "UPDATE asaas_status_pagamento_cartao_credito SET pago = true WHERE id = ?1")
	void marcarPagamentoCartaoCreditoComoPagoPorId(Long id);
	
}
