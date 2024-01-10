package com.joseleonardo.lojavirtual.api.pagamento.assas.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.joseleonardo.lojavirtual.LojaVirtualApiApplication;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.service.AsaasPagamentoService;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApiApplication.class)
public class AsaasPagamentoServiceTest {

	@Autowired
	private AsaasPagamentoService asaasPagamentoService;
	
	@Test
	public void testCriarChavePix() throws Exception {
		String chavePix = asaasPagamentoService.criarChavePix();
		
		System.out.println("Chave Pix: " + chavePix);
	}
	
}
