package com.joseleonardo.lojavirtual.security;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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

		/* Usado para para teste no Postman */
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}

}
