package com.joseleonardo.lojavirtual.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.joseleonardo.lojavirtual.model.Usuario;
import com.joseleonardo.lojavirtual.repository.UsuarioRepository;

@Service
public class TarefaAutomatizadaService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EnvioEmailService envioEmailService;

	//@Scheduled(initialDelay = 2000, fixedDelay = 86400000) /* Roda a cada 24 horas */
	@Scheduled(cron = "0 0 11 * * *", zone = "America/Sao_Paulo") /* Vai roda todos os dias as 11 horas da manhã no horário São Paulo */
	public void notificarUsuarioParaTrocarDeSenha() throws UnsupportedEncodingException, MessagingException, InterruptedException {
		List<Usuario> usuariosComSenhaVencida = usuarioRepository.usuariosComSenhaVencida();

		for (Usuario usuario : usuariosComSenhaVencida) {
			StringBuilder msg = new StringBuilder();
			msg.append("Olá, " + usuario.getPessoa().getNome() + "<br/>");
			msg.append("Está na hora de trocar a sua senha, já passou os 90 dias de validade.<br/>");
			msg.append("Troque sua senha na loja virtual do José Leonardo");

			envioEmailService.enviarEmailHtml("Troca de senha", msg.toString(), usuario.getLogin());

			/* Da uma pause de 3s para enviar o próximo e-mail/rodar o for each */
			Thread.sleep(3000);
		}
	}

}
