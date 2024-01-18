package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.AsaasBoletoPix;

@Repository
@Transactional
public interface AsaasBoletoPixRepository extends JpaRepository<AsaasBoletoPix, Long> {

	@Query("SELECT abp FROM AsaasBoletoPix abp "
		 + "WHERE abp.vendaCompraLojaVirtual.id = ?1 AND abp.pago = false")
	List<AsaasBoletoPix> buscarBoletosNaoPagosPorIdVenda(Long idVenda);
	
}
