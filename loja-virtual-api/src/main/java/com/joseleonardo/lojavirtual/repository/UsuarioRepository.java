package com.joseleonardo.lojavirtual.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.joseleonardo.lojavirtual.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	@Query("SELECT u FROM Usuario u WHERE u.login = ?1")
	Usuario buscarUsuarioPorLogin(String login);

	@Query("SELECT u FROM Usuario u WHERE u.pessoa.id = ?1 OR u.login = ?2")
	Usuario buscarUsuarioPorPessoaIdOuLogin(Long id, String email);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, 
	       value = "INSERT INTO usuario_acesso (usuario_id, acesso_id) "
	       		 + "VALUES (?1, (SELECT id FROM Acesso WHERE nome = 'ROLE_USUARIO'))")
	void inserirAcessoDeUsuarioParaPessoaJuridica(Long id);
	
}
