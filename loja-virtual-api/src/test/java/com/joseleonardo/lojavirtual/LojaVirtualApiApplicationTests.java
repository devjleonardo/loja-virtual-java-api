package com.joseleonardo.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.joseleonardo.lojavirtual.controller.AcessoController;
import com.joseleonardo.lojavirtual.model.Acesso;

@SpringBootTest(classes = LojaVirtualApiApplication.class)
class LojaVirtualApiApplicationTests {
	
	@Autowired
	private AcessoController acessoController;

	@Test
	public void testCadastrarAcesso() {
		Acesso acesso = new Acesso();
		acesso.setNome("ROLE_ADMIN");
		
		acessoController.salvarAcesso(acesso);
	}

}
