package com.joseleonardo.lojavirtual.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.joseleonardo.lojavirtual.ApplicationContextLoad;
import com.joseleonardo.lojavirtual.model.Usuario;
import com.joseleonardo.lojavirtual.repository.UsuarioRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/* Criar e retonar a autenticação JWT */
@Service
public class JWTTokenAutenticacaoService {

	/* Token de validade de 11 dias */
	private static final long EXPIRATION_TIME = 959990000;

	/* Chave de senha para juntar com o JWT (token) */
	private static final String SECRET = "asd/-epq-qd/eqwlqf8*as@..,$";

	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	/* Gera o token e da a resposta para o cliente com JWT */
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {
		/* Montagem do token */
		String jwt = Jwts.builder() /* Chama o gerador de token */
				.setSubject(username) /* Adiciona o login do usuário ao token */
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /* Tempo de expiração do token */
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();

		/* Exemplo: Bearer *-/a*dad9s5d6as5d4s5d4s45dsd54s.sd4s4d45s45d4sd54d45s4d5s.ds5d5s5d5s65d6s6d */
		String token = TOKEN_PREFIX + " " + jwt;

		/* Dá a resposta pra tela e para o cliente, outra API, navegador, aplicativo, javascript, outra chamada java, etc... */
		response.addHeader(HEADER_STRING, token);
		
		liberacaoDeCors(response);

		/* Usado para para teste no Postman */
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}
	
	/* Pega o login do usuário na validação do token, se o login for válido 
	 * retorna o usuário por completo, caso não seja válido retorna null */
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = request.getHeader(HEADER_STRING);
		
		try {
			if (token != null) {
				String tokenLimpo = token.replace(TOKEN_PREFIX, "");
				
				/* Faz a validação do token e obtém o login do usuário contido 
				 * no campo subject do token */
				String loginRetornadoDoToken = Jwts.parser()
				        .setSigningKey(SECRET)
						.parseClaimsJws(tokenLimpo)
						.getBody()
						.getSubject(); /* ADMIN ou Alex */
				
				if (loginRetornadoDoToken != null) {
					Usuario usuario = ApplicationContextLoad.getApplicationContext()
							.getBean(UsuarioRepository.class)
							.buscarUsuarioPorLogin(loginRetornadoDoToken);
					
					if (usuario != null) {
						return new UsernamePasswordAuthenticationToken(
								usuario.getLogin(), usuario.getSenha(), usuario.getAuthorities());
					}
				}
			}
		} catch (SignatureException e) {
			response.getWriter().write("O token está inválido");
		} catch (ExpiredJwtException e) {
			response.getWriter().write("O token está expirado, efetue o login novamente");
		} finally {
			liberacaoDeCors(response);
		}
		
		return null;
	}
	
	/* Fazendo liberação contra erro de CORS no navegador */
	private void liberacaoDeCors(HttpServletResponse response) {
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}

		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}

		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
	}

}
