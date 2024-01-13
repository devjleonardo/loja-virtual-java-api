package com.joseleonardo.lojavirtual.api.integracao.pagamento.assas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.joseleonardo.lojavirtual.LojaVirtualApiApplication;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.criar_novo_cliente.AsaasPagamentoCriarNovoClienteDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.service.AsaasPagamentoService;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApiApplication.class)
public class AsaasPagamentoServiceTest {

	@Autowired
	private AsaasPagamentoService asaasPagamentoService;
	
	@Test
	public void testCriarNovoCliente() throws Exception {
		AsaasPagamentoCriarNovoClienteDTO novoClienteDTO = new AsaasPagamentoCriarNovoClienteDTO();
		novoClienteDTO.setName("José Leonardo");
		novoClienteDTO.setCpfCnpj("07808516090");
		novoClienteDTO.setEmail("leoprogamer57@gmail.com");
		novoClienteDTO.setMobilePhone("(44) 99786-4523");
		
		String idCliente = asaasPagamentoService.criarNovoCliente(novoClienteDTO);
		
		assertEquals("cus_000005847814", idCliente);
	}
	
	@Test
	public void testCriarChavePix() throws Exception {
		// Chamando o método de criação de chave pix no serviço e obtendo o retorno da API
		String chavePix = asaasPagamentoService.criarChavePix();
		
		// Imprimindo o retorno da API no console
		System.out.println("Chave Pix: " + chavePix);
	}
	
}
