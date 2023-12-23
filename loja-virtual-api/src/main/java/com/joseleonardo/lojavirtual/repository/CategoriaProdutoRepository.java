package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.CategoriaProduto;

@Repository
@Transactional
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

	@Query("SELECT cp FROM CategoriaProduto cp WHERE UPPER(TRIM(cp.nome)) LIKE %?1%")
	List<CategoriaProduto> buscarCategoriaProdutoPorNome(String nome);
	
	@Query(nativeQuery = true, 
		   value = "SELECT COUNT(1) > 0 FROM categoria_produto "
				 + "WHERE UPPER(TRIM(nome)) = UPPER(TRIM(?1))")
	boolean existeCategoriaProdutoCadastradaComNome(String nome);

}
