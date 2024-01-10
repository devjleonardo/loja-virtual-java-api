package com.joseleonardo.lojavirtual;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.joseleonardo.lojavirtual.api.controller.PessoaFisicaController;
import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.Endereco;
import com.joseleonardo.lojavirtual.model.PessoaFisica;
import com.joseleonardo.lojavirtual.model.PessoaJuridica;
import com.joseleonardo.lojavirtual.model.enums.TipoEndereco;
import com.joseleonardo.lojavirtual.repository.PessoaJuridicaRepository;

import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApiApplication.class)
public class PessoaFisicaTest extends TestCase {
	
	@Autowired
	private PessoaFisicaController pessoaFisicaController;
	
	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	@Test
	public void testCadastrarPessoaFisica() throws LojaVirtualException {
		PessoaJuridica empresa = pessoaJuridicaRepository.existeCnpjCadastrado("18475547000109");
		
		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setCpf("19903444028");
		pessoaFisica.setNome("Alice Spilman Temperini");
		pessoaFisica.setEmail("alice_temperini@inovartech.com");
		pessoaFisica.setTelefone("(41) 99227-4839");
		pessoaFisica.setTipoPessoa("FISICA");
		pessoaFisica.setEmpresa(empresa);
		
		Endereco endereco1 = new Endereco();
		endereco1.setCep("07.152-816");
		endereco1.setLogradouro("Rua Monte das Oliveiras");
		endereco1.setBairro("Jardim Oliveira II");
		endereco1.setNumero("389");
		endereco1.setComplemento("Sobrado");
		endereco1.setCidade("Guarulhos");
		endereco1.setUf("SP");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setPessoa(pessoaFisica);
		endereco1.setEmpresa(empresa);

		pessoaFisica.getEnderecos().add(endereco1);

		Endereco endereco2 = new Endereco();
		endereco2.setCep("04.467-150");
		endereco2.setLogradouro("Rua Wilson Cantoni");
		endereco2.setBairro("Parque Primavera");
		endereco2.setNumero("896");
		endereco2.setComplemento("Casa");
		endereco2.setCidade("São Paulo");
		endereco2.setUf("SP");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setPessoa(pessoaFisica);
		endereco2.setEmpresa(empresa);

		pessoaFisica.getEnderecos().add(endereco2);

		pessoaFisica = pessoaFisicaController.salvarPessoaFisica(pessoaFisica).getBody();

		assertEquals(true, pessoaFisica.getId() > 0);

		for (Endereco endereco : pessoaFisica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}

		assertEquals(2, pessoaFisica.getEnderecos().size());
	}

}
