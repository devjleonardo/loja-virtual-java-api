package com.joseleonardo.lojavirtual;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.joseleonardo.lojavirtual.controller.AcessoController;
import com.joseleonardo.lojavirtual.model.Acesso;
import com.joseleonardo.lojavirtual.repository.AcessoRepository;

import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtualApiApplication.class)
class LojaVirtualApiApplicationTests extends TestCase {
	
	@Autowired
	private AcessoController acessoController;
	
	@Autowired
	private AcessoRepository acessoRepository;

	@Test
	public void testCadastrarAcesso() {
		Acesso acesso = new Acesso();
		acesso.setNome("ROLE_ADMIN");
		
		assertEquals(true, acesso.getId() == null);
		
		/* Salvou no banco de dados */
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		/* Validar se os dados foram salvos da forma correta */
		assertEquals(true, acesso.getId() > 0);
		assertEquals("ROLE_ADMIN", acesso.getNome());
		
		
		/* Teste de buscar um acesso por id */
		Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();
		
		assertEquals(acesso.getId(), acesso2.getId());
		
		
		/* Teste de deletar um acesso por id */
		acessoRepository.deleteById(acesso2.getId());
		
		/* Libera todas as alterações pendentes no banco de dados
		 * inclusive o deletar um acesso por id */
		acessoRepository.flush();
		
		Acesso acesso3 = acessoRepository.findById(acesso2.getId()).orElse(null);
		
		assertEquals(true, acesso3 == null);
		
		
		/* Teste de buscar acesso por nome */
		acesso = new Acesso();
		acesso.setNome("ROLE_ALUNO");
		
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		List<Acesso> acessos = acessoRepository.buscarAcessoPorNome("ALUNO".trim().toUpperCase());
		
		assertEquals(1, acessos.size());
		
		acessoRepository.deleteById(acesso.getId());
	}

}
