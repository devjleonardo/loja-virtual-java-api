package com.joseleonardo.lojavirtual.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.joseleonardo.lojavirtual.model.PessoaJuridica;

@Repository
public interface PessoaJuridicaRepository extends CrudRepository<PessoaJuridica, Long> {

	@Query("SELECT pj FROM PessoaJuridica pj WHERE pj.cnpj = ?1")
	PessoaJuridica existeCnpjCadastrado(String cnpj);

	@Query(nativeQuery = true, 
			value = 
			  "SELECT constraint_name "
			+ "FROM   information_schema.constraint_column_usage "
			+ "WHERE  table_name = 'usuario_acesso' "
			+ "       AND column_name = 'acesso_id' "
			+ "       AND constraint_name <> 'unique_usuario_acesso'")
	String consultaConstraintAcesso();
	
}
