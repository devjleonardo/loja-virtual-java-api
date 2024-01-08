package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.Endereco;

@Repository
@Transactional
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

	@Query("SELECT e FROM Endereco e WHERE e.pessoa.id =?1")
	List<Endereco> buscarEnderecoPorPessoaId(Long pessoaId);
	
}
