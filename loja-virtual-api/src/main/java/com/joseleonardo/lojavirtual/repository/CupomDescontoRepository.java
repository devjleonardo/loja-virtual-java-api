package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.CupomDesconto;

@Repository
@Transactional
public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Long> {

	@Query("SELECT cd FROM CupomDesconto cd WHERE cd.empresa.id = ?1")
	List<CupomDesconto> buscarCupomDescontoPorEmpresaId(Long empresaId);
	
}
