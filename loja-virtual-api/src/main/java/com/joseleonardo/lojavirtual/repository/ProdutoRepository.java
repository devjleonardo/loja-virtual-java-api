package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.Produto;

@Repository
@Transactional
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	@Query("SELECT p FROM Produto p WHERE UPPER(TRIM(p.nome)) LIKE %?1%")
	List<Produto> buscarProdutoPorNome(String nome);

	@Query(nativeQuery = true, 
		   value = "SELECT COUNT(1) > 0 FROM produto "
			     + "WHERE UPPER(TRIM(nome)) = UPPER(TRIM(?1)) AND empresa_id = ?2")
	boolean existeProdutoComMesmoNomeNaEmpresaId(String nome, Long empresaId);

}
