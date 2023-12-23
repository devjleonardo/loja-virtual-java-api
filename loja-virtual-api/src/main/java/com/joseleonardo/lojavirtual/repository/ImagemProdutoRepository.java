package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.joseleonardo.lojavirtual.model.ImagemProduto;

@Repository
@Transactional
public interface ImagemProdutoRepository extends JpaRepository<ImagemProduto, Long> {

	@Query("SELECT ip FROM ImagemProduto ip WHERE ip.produto.id = ?1")
	List<ImagemProduto> buscarImagemProdutoPorProdutoId(Long produtoId);
	
	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(nativeQuery = true,
		   value = "DELETE FROM imagem_produto WHERE produto_id = ?1")
	void deletarTodasImagemProdutoPorProdutoId(Long produtoId);
	
}
