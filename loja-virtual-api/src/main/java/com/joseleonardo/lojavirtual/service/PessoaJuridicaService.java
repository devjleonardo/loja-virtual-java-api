package com.joseleonardo.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.joseleonardo.lojavirtual.model.PessoaJuridica;
import com.joseleonardo.lojavirtual.model.Usuario;
import com.joseleonardo.lojavirtual.repository.PessoaJuridicaRepository;
import com.joseleonardo.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaJuridicaService {

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public PessoaJuridica salvar(PessoaJuridica  pessoaJuridica) {
		//pessoaJuridica = pessoaJuridicaRepository.save(pessoaJuridica);

		for (int i = 0; i < pessoaJuridica.getEnderecos().size(); i++) {
			pessoaJuridica.getEnderecos().get(i).setPessoa(pessoaJuridica);
			pessoaJuridica.getEnderecos().get(i).setEmpresa(pessoaJuridica);
		}
		
		pessoaJuridica = pessoaJuridicaRepository.save(pessoaJuridica);
		
		Usuario usuarioPessoaJuridica = usuarioRepository.buscarUsuarioPorPessoaIdOuLogin(
				pessoaJuridica.getId(), pessoaJuridica.getEmail());
		
		if (usuarioPessoaJuridica == null) {
			String constraint = pessoaJuridicaRepository.consultaConstraintAcesso();
			
			if (constraint != null) {
				jdbcTemplate.execute("BEGIN; "
						           + "ALTER TABLE usuario_acesso DROP CONSTRAINT " 
			                       + constraint + "; "
			                       + "COMMIT;");
			}
			
			usuarioPessoaJuridica = new Usuario();
			usuarioPessoaJuridica.setLogin(pessoaJuridica.getEmail());
			
			String senha = "" + Calendar.getInstance().getTimeInMillis();
			String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);
			
			usuarioPessoaJuridica.setSenha(senhaCriptografada);
			usuarioPessoaJuridica.setDataAtualizacaoSenha(Calendar.getInstance().getTime());
			usuarioPessoaJuridica.setEmpresa(pessoaJuridica);
			usuarioPessoaJuridica.setPessoa(pessoaJuridica);
			
			usuarioPessoaJuridica = usuarioRepository.save(usuarioPessoaJuridica);
			
			usuarioRepository.inserirAcessoDeUsuarioParaPessoaJuridica(usuarioPessoaJuridica.getId());
			
			/* Fazer o envio de e-mail do login e da senha */
		}
		
		return pessoaJuridica;
	}
	
}
