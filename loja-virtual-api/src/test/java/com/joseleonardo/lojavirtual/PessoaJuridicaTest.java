package com.joseleonardo.lojavirtual;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.joseleonardo.lojavirtual.controller.PessoaJuridicaController;
import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
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

		pessoaJuridicaController.salvar(pessoaJuridica);
	}

}
