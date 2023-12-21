package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.joseleonardo.lojavirtual.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	@Query("SELECT u FROM Usuario u WHERE u.dataAtualizacaoSenha <= current_date - 90")
	List<Usuario> usuariosComSenhaVencida();

	@Query("SELECT u FROM Usuario u WHERE u.login = ?1")
	Usuario buscarUsuarioPorLogin(String login);

	@Query("SELECT u FROM Usuario u WHERE u.pessoa.id = ?1 OR u.login = ?2")
	Usuario buscarUsuarioPorPessoaIdOuLogin(Long id, String email);
	
	@Query(nativeQuery = true, 
			value = 
			  "SELECT constraint_name "
			+ "FROM   information_schema.constraint_column_usage "
			+ "WHERE  table_name = 'usuario_acesso' "
			+ "       AND column_name = 'acesso_id' "
			+ "       AND constraint_name <> 'unique_usuario_acesso'")
	String consultarConstraintNaTabelaUsuarioAcesso();

	@Transactional
	@Modifying
	@Query(nativeQuery = true, 
	       value = "INSERT INTO usuario_acesso (usuario_id, acesso_id) "
	       		 + "VALUES (?1, (SELECT id FROM Acesso WHERE nome = 'ROLE_USUARIO'))")
	void inserirAcessoDeUsuario(Long id);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, 
	       value = "INSERT INTO usuario_acesso (usuario_id, acesso_id) VALUES (?1, "
	        	 + "(SELECT id FROM Acesso WHERE nome = ?2 LIMIT 1)) ")
	void inserirQualquerAcesso(Long id, String acesso);

}
