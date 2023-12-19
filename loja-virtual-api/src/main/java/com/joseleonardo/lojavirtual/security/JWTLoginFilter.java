package com.joseleonardo.lojavirtual.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joseleonardo.lojavirtual.model.Usuario;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	/* Configurando o gerenciador de autenticação */
	public JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
		/* Obriga a autenticar a URL */
		super(new AntPathRequestMatcher(url));

		/* Gerenciador de autenticação */
		setAuthenticationManager(authenticationManager);
	}

	/* Retorna o usuário ao processar a autenticação */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		/* Obter o usuário */
		Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

		/* Retorna o usuário com login e senha */
		return getAuthenticationManager()
				.authenticate(
						new UsernamePasswordAuthenticationToken(
								usuario.getLogin(), usuario.getSenha()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		try {
			new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
