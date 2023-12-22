package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.joseleonardo.lojavirtual.model.NotaItemProduto;

@Repository
@Transactional
public interface NotaItemProdutoRepository extends JpaRepository<NotaItemProduto, Long> {
	
	@Query("SELECT nip FROM NotaItemProduto nip "
		 + "WHERE nip.notaFiscalCompra.id = ?1 AND nip.produto.id = ?2")
	List<NotaItemProduto> buscarNotaItemProdutoPorNotaFiscalCompraIdEProdutoId(
			Long notaFiscalCompraId, Long produtoId);
	
	@Query("SELECT nip FROM NotaItemProduto nip WHERE nip.produto.id = ?1")
	List<NotaItemProduto> buscarNotaItemProdutoPorProdutoId(Long produtoId);
	
	@Query("SELECT nip FROM NotaItemProduto nip WHERE nip.notaFiscalCompra.id = ?1")
	List<NotaItemProduto> buscarNotaItemProdutoPorNotaFiscalCompraId(Long notaFiscalCompraId);
	
	@Query("SELECT nip FROM NotaItemProduto nip WHERE nip.empresa.id = ?1")
	List<NotaItemProduto> buscarNotaItemProdutoPorEmpresaId(Long empresaId);
	
	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(nativeQuery = true, 
		   value = "DELETE FROM nota_item_produto "
		   		 + "WHERE nota_fiscal_compra_id = ?1")
	void deletarNotaItemProdutoPorNotaFiscalCompraId(Long notaFiscalCompraId);
	
}
