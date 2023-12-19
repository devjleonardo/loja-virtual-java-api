package com.joseleonardo.lojavirtual;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.joseleonardo.lojavirtual.controller.PessoaJuridicaController;
import com.joseleonardo.lojavirtual.enums.TipoEndereco;
import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.Endereco;
import com.joseleonardo.lojavirtual.model.PessoaJuridica;

import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApiApplication.class)
public class PessoaJuridicaTest extends TestCase {
	
	@Autowired
	private PessoaJuridicaController pessoaJuridicaController;
	
	@Test
	public void testCadastrarPessoaJuridica() throws LojaVirtualException {
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("Lojas InovarTech");
		pessoaJuridica.setEmail("inovar.tech2@inovartech.com");
		pessoaJuridica.setTelefone("(44) 9.9575-9994");
		pessoaJuridica.setInscricaoEstadual("3954911569");
		pessoaJuridica.setRazaoSocial("Alice & Carlin Lojas InovarTech LTDA");
		pessoaJuridica.setNomeFantasia("Lojas InovarTech");
		pessoaJuridica.setTipoPessoa("JURIDICA");
		
		Endereco endereco1 = new Endereco();
		endereco1.setCep("07.152-816");
		endereco1.setLogradouro("Rua Monte das Oliveiras");
		endereco1.setBairro("Jardim Oliveira II");
		endereco1.setNumero("389");
		endereco1.setComplemento("Sobrado");
		endereco1.setCidade("Guarulhos");
		endereco1.setUf("SP");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setPessoa(pessoaJuridica);
		endereco1.setEmpresa(pessoaJuridica);

		pessoaJuridica.getEnderecos().add(endereco1);

		Endereco endereco2 = new Endereco();
		endereco2.setCep("04.467-150");
		endereco2.setLogradouro("Rua Wilson Cantoni");
		endereco2.setBairro("Parque Primavera");
		endereco2.setNumero("896");
		endereco2.setComplemento("Casa");
		endereco2.setCidade("São Paulo");
		endereco2.setUf("SP");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setPessoa(pessoaJuridica);
		endereco2.setEmpresa(pessoaJuridica);

		pessoaJuridica.getEnderecos().add(endereco2);

		pessoaJuridica = pessoaJuridicaController.salvar(pessoaJuridica).getBody();
		
		assertEquals(true, pessoaJuridica.getId() > 0);

		for (Endereco endereco : pessoaJuridica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}

		assertEquals(2, pessoaJuridica.getEnderecos().size());
	}

}
