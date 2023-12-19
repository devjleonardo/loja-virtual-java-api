package com.joseleonardo.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.joseleonardo.lojavirtual.model.PessoaJuridica;
import com.joseleonardo.lojavirtual.repository.PessoaJuridicaRepository;

import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApiApplication.class)
public class PessoaJuridicaTest extends TestCase {
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Test
	public void testCadastrarPessoaJuridica() {
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("18475547000109");
		pessoaJuridica.setNome("Lojas InovarTech");
		pessoaJuridica.setEmail("inovar.tech@inovartech.com");
		pessoaJuridica.setTelefone("(44) 9.9575-9994");
		pessoaJuridica.setInscricaoEstadual("3954911569");
		pessoaJuridica.setRazaoSocial("Alice & Carlin Lojas InovarTech LTDA");
		pessoaJuridica.setNomeFantasia("Lojas InovarTech");
		pessoaJuridica.setTipoPessoa("JURIDICA");

		pessoaJuridicaRepository.save(pessoaJuridica);
	}

}
