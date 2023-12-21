package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.joseleonardo.lojavirtual.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	@Query("SELECT p FROM Produto p WHERE UPPER(TRIM(p.nome)) LIKE %?1% AND p.empresa.id = ?2")
	List<Produto> buscarProdutoPorNomeEPorEmpresaId(String nome, Long empresaId);
	
	@Query("SELECT p FROM Produto p WHERE UPPER(TRIM(p.nome)) LIKE %?1%")
	List<Produto> buscarProdutoPorNome(String nome);

}
