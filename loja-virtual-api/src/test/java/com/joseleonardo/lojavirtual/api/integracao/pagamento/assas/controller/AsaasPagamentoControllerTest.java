package com.joseleonardo.lojavirtual.api.integracao.pagamento.assas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.joseleonardo.lojavirtual.LojaVirtualApiApplication;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.controller.AsaasPagamentoController;

import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApiApplication.class)
public class AsaasPagamentoControllerTest extends TestCase {
	
	@Autowired
	private AsaasPagamentoController asaasPagamentoController;
	
	@Test
	public void testFinalizarCompraCartaoAsaas() throws Exception {
		asaasPagamentoController
		    .finalizarCompraCartaoAsaas(
		        "4210 9980 5195 9396", "Laís E L Cardoso", 
				"234", "07", 
				"2027", 37L, 
				"18806081039", 1, 
				"96835743", "Rua Vereador Rudi Müller", 
				"487", "RS", "Santa Cruz do Sul");
	}

}
