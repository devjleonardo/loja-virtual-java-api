package com.joseleonardo.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.joseleonardo.lojavirtual.model.PessoaFisica;
import com.joseleonardo.lojavirtual.model.Usuario;
import com.joseleonardo.lojavirtual.repository.PessoaFisicaRepository;
import com.joseleonardo.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaFisicaService {
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private EnvioEmailService envioEmailService;
	
	public PessoaFisica salvar(PessoaFisica pessoaFisica) {
		for (int i = 0; i < pessoaFisica.getEnderecos().size(); i++) {
			pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
			//pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
		}
		
		pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);

		Usuario usuarioPessoaFisica = usuarioRepository
				.buscarUsuarioPorPessoaIdOuLogin(pessoaFisica.getId(), pessoaFisica.getEmail());

		if (usuarioPessoaFisica == null) {
			String constraint = usuarioRepository.consultarConstraintNaTabelaUsuarioAcesso();

			if (constraint != null) {
				jdbcTemplate.execute("BEGIN; "
						           + "ALTER TABLE usuario_acesso DROP CONSTRAINT " 
	                               + constraint + "; "
	                               + "COMMIT;");
			}

			usuarioPessoaFisica = new Usuario();
			usuarioPessoaFisica.setLogin(pessoaFisica.getEmail());

			String senha = "" + Calendar.getInstance().getTimeInMillis();
			String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);

			usuarioPessoaFisica.setSenha(senhaCriptografada);
			usuarioPessoaFisica.setDataAtualizacaoSenha(Calendar.getInstance().getTime());
			usuarioPessoaFisica.setEmpresa(pessoaFisica.getEmpresa());
			usuarioPessoaFisica.setPessoa(pessoaFisica);

			usuarioPessoaFisica = usuarioRepository.save(usuarioPessoaFisica);

			usuarioRepository.inserirAcessoDeUsuario(usuarioPessoaFisica.getId());

			StringBuilder mensagemHtml = new StringBuilder();
			mensagemHtml.append("<b>Segue abaixo seus dados de acesso para a Loja Virtual</b><br/>");
			mensagemHtml.append("<b>Login: </b>" + pessoaFisica.getEmail() + "<br/>");
			mensagemHtml.append("<b>Senha: </b>" + senha + "<br/><br/>");
			mensagemHtml.append("Obrigado!");

			try {
				envioEmailService.enviarEmailHtml("Acesso gerado para Loja Virtual", 
						mensagemHtml.toString(), pessoaFisica.getEmail());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return pessoaFisica;
	}

}
