package com.joseleonardo.lojavirtual.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.joseleonardo.lojavirtual.model.NotaItemProduto;

@Repository
@Transactional
public interface NotaItemProdutoRepository extends JpaRepository<NotaItemProduto, Long> {

	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(nativeQuery = true, 
		   value = "DELETE FROM nota_item_produto "
		   		 + "WHERE nota_fiscal_compra_id = ?1")
	void deletarNotaFiscalCompraPorId(Long notaFiscalCompraId);

}
