package com.joseleonardo.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.joseleonardo.lojavirtual.model.Usuario;
import com.joseleonardo.lojavirtual.repository.UsuarioRepository;

@Service
public class ImplementacaoUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.buscarUsuarioPorLogin(username); /* Recebe o login pra consultar */

		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não foi encontrado");
		}

		return new User(usuario.getLogin(), usuario.getSenha(), usuario.getAuthorities());
	}

}
