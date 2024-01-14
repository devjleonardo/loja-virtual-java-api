package com.joseleonardo.lojavirtual.api.integracao.pagamento.assas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.joseleonardo.lojavirtual.LojaVirtualApiApplication;
import com.joseleonardo.lojavirtual.api.dto.venda.VendaCobrancaDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.service.AsaasPagamentoService;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApiApplication.class)
public class AsaasPagamentoServiceTest {

	@Autowired
	private AsaasPagamentoService asaasPagamentoService;
	
	@Test
	public void testCriarNovaCobranca() throws Exception {
		VendaCobrancaDTO vendaCobrancaDTO = new VendaCobrancaDTO();
		vendaCobrancaDTO.setNomeCliente("José Leonardo");
		vendaCobrancaDTO.setCpfCnpjCliente("07808516090");
		vendaCobrancaDTO.setEmailCliente("leoprogamer57@gmail.com");
		vendaCobrancaDTO.setTelefoneCliente("(44) 99786-4523");
		vendaCobrancaDTO.setIdVenda(37L);
		
		String retorno = asaasPagamentoService.criarNovaCobranca(vendaCobrancaDTO);
		
		System.out.println(retorno);
	}
	
	@Test
	public void testBuscarOuCriarNovoCliente() throws Exception {
		VendaCobrancaDTO vendaCobrancaDTO = new VendaCobrancaDTO();
		vendaCobrancaDTO.setNomeCliente("José Leonardo");
		vendaCobrancaDTO.setCpfCnpjCliente("07808516090");
		vendaCobrancaDTO.setEmailCliente("leoprogamer57@gmail.com");
		vendaCobrancaDTO.setTelefoneCliente("(44) 99786-4523");
		
		String idCliente = asaasPagamentoService.buscarOuCriarNovoCliente(vendaCobrancaDTO);
		
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
