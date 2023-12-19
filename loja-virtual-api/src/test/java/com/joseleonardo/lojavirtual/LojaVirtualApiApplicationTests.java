package com.joseleonardo.lojavirtual;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joseleonardo.lojavirtual.controller.AcessoController;
import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.Acesso;
import com.joseleonardo.lojavirtual.repository.AcessoRepository;

import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApiApplication.class)
class LojaVirtualApiApplicationTests extends TestCase {
	
	@Autowired
	private AcessoController acessoController;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	/* Teste do end-point de salvar acesso */
	@Test
	public void testRestApiSalvarAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders
				.webAppContextSetup(this.webApplicationContext);
		
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setNome("ROLE_TESTE_SALVAR_ACESSO");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.post("/salvarAcesso")
		        .content(objectMapper.writeValueAsString(acesso))
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON));
		
		System.out.println("Retorno da API: " + retornoApi.andReturn()
		        .getResponse().getContentAsString());
		
		/* Converter o retorno da API para um objeto de cesso */
		Acesso objetoRetorno = objectMapper
				.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);
	            
		assertEquals(acesso.getNome(), objetoRetorno.getNome());
	}
	
	@Test
	public void testRestApiDeletarAcessoPorId() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders
				.webAppContextSetup(this.webApplicationContext);
		
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setNome("ROLE_TESTE_DELETAR_ACESSO_POR_ID");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.delete("/deletarAcessoPorId/" + acesso.getId())
		        .content(objectMapper.writeValueAsString(acesso))
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON));
		
		System.out.println("Retorno da API: " + retornoApi.andReturn()
		    .getResponse().getContentAsString());
		
		System.out.println("Status de retorno: " + retornoApi.andReturn()
		    .getResponse().getStatus());
	
		assertEquals("Acesso removido", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	}
	
	@Test
	public void testRestApiBuscarAcessoPorId() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders
				.webAppContextSetup(this.webApplicationContext);
		
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setNome("ROLE_TESTE_BUSCAR_ACESSO_POR_ID");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.get("/buscarAcessoPorId/" + acesso.getId())
		        .content(objectMapper.writeValueAsString(acesso))
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON));
		
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
		Acesso acessoRetorno = objectMapper
		        .readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);
		
		assertEquals(acesso.getId(), acessoRetorno.getId());
		assertEquals(acesso.getNome(), acessoRetorno.getNome());
	}
	
	@Test
	public void testRestApiBuscarAcessoPorNome() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.webApplicationContext);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		acesso.setNome("ROLE_TESTE_BUSCAR_ACESSO_POR_NOME");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.get("/buscarAcessoPorNome/por_no")
		        .content(objectMapper.writeValueAsString(acesso))
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON));
		
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
		List<Acesso> retornoApiAcessos = objectMapper
				.readValue(retornoApi.andReturn().getResponse().getContentAsString(), 
						new TypeReference<List<Acesso>>() {});
		
		assertEquals(1, retornoApiAcessos.size());
		assertEquals(acesso.getNome(), retornoApiAcessos.get(0).getNome());
		
		acessoRepository.deleteById(acesso.getId());
	}

	@Test
	public void testCadastrarAcesso() throws LojaVirtualException {
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
