package com.joseleonardo.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AtualizacaoQuantidadeAcessoEndPointService {

	@Autowired
	private JdbcTemplate jdbcTemplate; 

	public void buscarPessoaFisicaPorNome() {
		jdbcTemplate.execute(
				  "BEGIN; "
				+ "UPDATE quantidade_acesso_end_point "
				+ "SET quantidade_acesso = quantidade_acesso + 1 "
				+ "WHERE end_point = 'buscarPessoaFisicaPorNome'; "
				+ "COMMIT;");
	}

}
