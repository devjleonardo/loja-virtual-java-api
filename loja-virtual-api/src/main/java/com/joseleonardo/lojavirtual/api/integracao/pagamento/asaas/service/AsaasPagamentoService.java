package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.constants.AsaasPagamentoConstants;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.criar_novo_cliente.AsaasPagamentoCriarNovoClienteDTO;
import com.joseleonardo.lojavirtual.ssl.HostIgnoringSSLClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class AsaasPagamentoService {
	
	@SuppressWarnings("unchecked")
	public String criarNovoCliente(AsaasPagamentoCriarNovoClienteDTO novoClienteDTO) throws Exception {
	    /* ID do cliente para ligar com a cobrança */
	    String idCliente = "";
	    
	    // Criando um cliente HTTP ignorando SSL
	    Client client = new HostIgnoringSSLClient(
	        AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX
	    ).createUnsecuredHttpClient();
	    
	    // Definindo o recurso da API da Asaas para buscar cliente pelo e-mail
	    WebResource webResource = client.resource(
	        AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX + "customers?email=" + novoClienteDTO.getEmail()
	    );
	    
	    // Enviando uma requisição GET para a API Asaas
	    ClientResponse clientResponse = webResource
	        .accept("application/json;charset=UTF-8")
	        .header("Content-Type", "application/json")
	        .header("access_token", AsaasPagamentoConstants.ASAAS_ACCESS_TOKEN_SANDBOX)
	        .get(ClientResponse.class);
	    
	    // Parser para converter a resposta JSON em um mapa
	    LinkedHashMap<String, Object> parser = new JSONParser(
	        clientResponse.getEntity(String.class)
	    ).parseObject();
	    
	    // Obtendo o número total de resultados da busca
	    Integer totalCount = Integer.parseInt(parser.get("totalCount").toString());

	    // Verifica se não há clientes encontrados com o e-mail fornecido
	    if (totalCount <= 0) {
	        // Convertendo o objeto DTO para formato JSON
	        String json = new ObjectMapper().writeValueAsString(novoClienteDTO);
	        
	        // Criando um cliente HTTP ignorando SSL
	        Client clientNovoCliente = new HostIgnoringSSLClient(
	            AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX
	        ).createUnsecuredHttpClient();
	        
	        // Definindo o recurso da API da Asaas para criar um novo cliente
	        WebResource webResourceNovoCliente = clientNovoCliente.resource(
	            AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX + "customers"
	        );
	        
	        // Enviando uma requisição POST para a API Asaas
	        ClientResponse clientResponseNovoCliente = webResourceNovoCliente
	            .accept("application/json;charset=UTF-8")
	            .header("Content-Type", "application/json")
	            .header("access_token", AsaasPagamentoConstants.ASAAS_ACCESS_TOKEN_SANDBOX)
	            .post(ClientResponse.class, json);
	        
	        // Obtendo a resposta da criação do cliente
	        LinkedHashMap<String, Object> parserNovCliente = new JSONParser(
	            clientResponseNovoCliente.getEntity(String.class)
	        ).parseObject();
	        
	        clientResponseNovoCliente.close();
	        
	        // Obtendo o ID do cliente recém-criado
	        idCliente = parserNovCliente.get("id").toString();
	    } else { 
	        // Obtendo os dados do cliente existente
	    	List<Object> dadosCliente = (List<Object>) parser.get("data");
	        
	        // Extraindo o ID do cliente do JSON
	        idCliente = new Gson().toJsonTree(
	            dadosCliente.get(0)
	        ).getAsJsonObject().get("id").toString().replaceAll("\"", "");
	    }
	    
	    // Fechando a resposta da API Asaas
	    clientResponse.close();
	    
	    // Retornando o ID do cliente
	    return idCliente;
	}

	/**
	 * Criar a chave Pix dentro da nossa conta Asaas.
	 * 
	 * @return Chave
	 * @throws Exception
	 */
	public String criarChavePix() throws Exception {
		// Criando um cliente HTTP ignorando SSL
		Client client = new HostIgnoringSSLClient(
		    AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX
		).createUnsecuredHttpClient();
		
		// Definindo o recurso da API para criar uma chave Pix
		WebResource webResource = client.resource(
		    AsaasPagamentoConstants.ASAAS_API_BASE_URL_SANDBOX + "pix/addressKeys"
		);
		
		// Enviando uma requisição POST para a API GetResponse
		ClientResponse clientResponse = webResource
		    .accept("application/json;charset=UTF-8")
		    .header("Content-Type", "application/json")
		    .header("access_token", AsaasPagamentoConstants.ASAAS_ACCESS_TOKEN_SANDBOX)
		    .post(ClientResponse.class, "{\"type\":\"EVP\"}");
		
	    // Obtendo a resposta da API
	    String respostaApi = clientResponse.getEntity(String.class);
		
	    // Fechando a resposta do cliente
		clientResponse.close();
		
	    // Retornando a resposta da API Asaas
		return respostaApi;
	}
	
}