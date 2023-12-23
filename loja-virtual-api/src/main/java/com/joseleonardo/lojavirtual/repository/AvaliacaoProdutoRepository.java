package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.AvaliacaoProduto;

@Repository
@Transactional
public interface AvaliacaoProdutoRepository extends JpaRepository<AvaliacaoProduto, Long> {

	@Query("SELECT a FROM AvaliacaoProduto a WHERE a.produto.id = ?1")
	List<AvaliacaoProduto> buscarAvaliacaoProdutoPorProdutoId(Long produtoId);
	
	@Query("SELECT a FROM AvaliacaoProduto a WHERE a.pessoa.id = ?1")
	List<AvaliacaoProduto> buscarAvaliacaoProdutoPorPessoaId(Long pessoaId);
	
	@Query("SELECT a FROM AvaliacaoProduto a WHERE a.produto.id = ?1 AND a.pessoa.id = ?2")
	List<AvaliacaoProduto> buscarAvaliacaoProdutoPorProdutoIdEPessoaId(Long produtoId, 
			Long pessoaId);
	
}
