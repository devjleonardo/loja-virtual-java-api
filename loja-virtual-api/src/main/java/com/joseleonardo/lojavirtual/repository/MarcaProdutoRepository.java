package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.MarcaProduto;

@Repository
@Transactional
public interface MarcaProdutoRepository extends JpaRepository<MarcaProduto, Long> {

	@Query("SELECT mp FROM MarcaProduto mp WHERE UPPER(TRIM(mp.nome)) LIKE %?1%")
	List<MarcaProduto> buscarMarcaProdutoPorNome(String nome);

}
