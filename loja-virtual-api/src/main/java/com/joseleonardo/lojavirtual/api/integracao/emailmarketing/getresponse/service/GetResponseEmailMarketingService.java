package com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.constants.GetResponseEmailMarketingConstants;
import com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.dto.campanha.GetResponseEmailMarketingCampaignListDTO;
import com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.dto.contato.GetResponseEmailMarketingCriacaoContatoDTO;
import com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.dto.fromfields.GetResponseEmailMarketingFromFieldDTO;
import com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.dto.newsletter.GetResponseEmailMarketingCriacaoNewsletterDTO;
import com.joseleonardo.lojavirtual.ssl.HostIgnoringSSLClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class GetResponseEmailMarketingService {

	public List<GetResponseEmailMarketingCampaignListDTO> obterListaDeCampanhas() throws Exception {
	    // Criando um cliente HTTP ignorando SSL
	    Client client = new HostIgnoringSSLClient(
	        GetResponseEmailMarketingConstants.GET_RESPONSE_API_BASE_URL
	    ).createUnsecuredHttpClient();

	    // Definindo o recurso da API para obter a lista de campanhas
	    WebResource webResource = client.resource(
	        GetResponseEmailMarketingConstants.GET_RESPONSE_API_BASE_URL + "campaigns"
	    );

	    // Enviando a requisição GET para a API GetResponse e obtendo a resposta em formato JSON
	    String respostaJson = webResource
            .accept(MediaType.APPLICATION_JSON + ";charset=UTF-8")
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .header(
                "X-Auth-Token",
                GetResponseEmailMarketingConstants.GET_RESPONSE_ACCESS_TOKEN
            )
            .get(String.class);

	    // Criando um objeto ObjectMapper para realizar a conversão entre JSON e objetos Java
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    // Habilitando a configuração para aceitar um valor único como array ao desserializar JSON
	    objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

	    // Convertendo o JSON em uma lista de campanhas
	    List<GetResponseEmailMarketingCampaignListDTO> listaDeCampanhas = objectMapper.readValue(
	        respostaJson, new TypeReference<List<GetResponseEmailMarketingCampaignListDTO>>() {}
	    );

	    // Retornando a lista de campanhas
	    return listaDeCampanhas;
	}
	
	public String criarContato(GetResponseEmailMarketingCriacaoContatoDTO contatoDTO) throws Exception {
		// Convertendo o objeto para JSON
	    String json = new ObjectMapper().writeValueAsString(contatoDTO);

	    // Criando um cliente HTTP ignorando SSL
	    Client client = new HostIgnoringSSLClient(
	        GetResponseEmailMarketingConstants.GET_RESPONSE_API_BASE_URL
	    ).createUnsecuredHttpClient();

	    // Definindo o recurso da API para criação de contatos
	    WebResource webResource = client.resource(
	        GetResponseEmailMarketingConstants.GET_RESPONSE_API_BASE_URL + "contacts"
	    );

	    // Enviando uma requisição POST para a API GetResponse
	    ClientResponse clientResponse = webResource
            .accept("application/json;charset=UTF-8")
            .header("Content-Type", "application/json")
            .header(
                "X-Auth-Token", 
            	GetResponseEmailMarketingConstants.GET_RESPONSE_ACCESS_TOKEN
            )
            .post(ClientResponse.class, json);

	    // Obtendo a resposta da API
	    String respostaApi = clientResponse.getEntity(String.class);

	    // Verificando se o e-mail foi enviado com sucesso (código de status 202)
	    if (clientResponse.getStatus() == 202) {
	    	respostaApi = "Contato cadastrado com sucesso";
	    }
	    
	    // Fechando a resposta do cliente
	    clientResponse.close();

	    // Retornando a resposta da API GetResponse
	    return respostaApi;
	}
	
	public String enviarEmail(String idCampanha, String nomeEmail, String mensagem) throws Exception {
	    // Criando DTO para criação de newsletter
	    GetResponseEmailMarketingCriacaoNewsletterDTO criacaoNewsletterDTO = 
	        new GetResponseEmailMarketingCriacaoNewsletterDTO();
	    
	    // Configurando a campanha a ser associada
	    criacaoNewsletterDTO.getSendSettings().getSelectedCampaigns().add(idCampanha);
	    
	    // Configurando o assunto do e-mail
	    criacaoNewsletterDTO.setSubject("E-mail para teste de API");
	    
	    // Configurando o nome do e-mail
	    criacaoNewsletterDTO.setName(nomeEmail);
	    
	    // Configurando o ID do campo do remetente
	    criacaoNewsletterDTO.getFromField().setFromFieldId("rOj9H");
	    
	    // Configurando o ID do campo para resposta
	    criacaoNewsletterDTO.getReplyTo().setFromFieldId("rOj9H");
	    
	    // Configurando o ID da campanha
	    criacaoNewsletterDTO.getCampaign().setCampaignId(idCampanha);
	    
	    // Configurando a data e hora de envio (um dia a partir de agora)
	    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDateTime dataHoraAtual = LocalDateTime.now();
	    LocalDateTime amanha = dataHoraAtual.plusDays(1);
	    String dataDeEnvio = amanha.format(dateTimeFormatter);
	    criacaoNewsletterDTO.setSendOn(dataDeEnvio + "T18:20:52-03");
	    
	    // Configurando o conteúdo HTML do e-mail
	    criacaoNewsletterDTO.getContent().setHtml(mensagem);
	    
	    // Convertendo o objeto DTO para JSON
	    String json = new ObjectMapper().writeValueAsString(criacaoNewsletterDTO);
	    
	    // Criando um cliente HTTP ignorando SSL
	    Client client = new HostIgnoringSSLClient(
	        GetResponseEmailMarketingConstants.GET_RESPONSE_API_BASE_URL
	    ).createUnsecuredHttpClient();
	    
	    // Definindo o recurso da API para envio de newsletters
	    WebResource webResource = client.resource(
	        GetResponseEmailMarketingConstants.GET_RESPONSE_API_BASE_URL + "newsletters"
	    );
	    
	    // Enviando uma requisição POST para a API GetResponse
	    ClientResponse clientResponse = webResource
            .accept("application/json;charset=UTF-8")
            .header("Content-Type", "application/json")
            .header("X-Auth-Token", GetResponseEmailMarketingConstants.GET_RESPONSE_ACCESS_TOKEN)
            .post(ClientResponse.class, json);
	    
	    // Obtendo a resposta da API
	    String retornoApi = clientResponse.getEntity(String.class);
	    
	    // Verificando se o e-mail foi enviado com sucesso (código de status 201)
	    if (clientResponse.getStatus() == 202) {
	    	retornoApi = "Email enviado com sucesso";
	    }
	    
	    // Fechando a resposta do cliente
	    clientResponse.close();
	    
	    // Retornando a resposta da API GetResponse
	    return retornoApi;
	}
	
	// Método para obter a lista de campos de remetentes da API GetResponse
	public List<GetResponseEmailMarketingFromFieldDTO> obterListaCamposRemetentes() throws Exception {
	    // Criando um cliente HTTP ignorando SSL
	    Client client = new HostIgnoringSSLClient(
	        GetResponseEmailMarketingConstants.GET_RESPONSE_API_BASE_URL
	    ).createUnsecuredHttpClient();

	    // Criando o recurso da API para obtenção da lista de campos de remetentes
	    WebResource webResource = client.resource(
	        GetResponseEmailMarketingConstants.GET_RESPONSE_API_BASE_URL + "from-fields"
	    );

	    // Enviando a requisição GET para a API GetResponse e obtendo a resposta em formato JSON
	    String respostaJson = webResource
            .accept("application/json;charset=UTF-8")
            .header("Content-Type", "application/json")
            .header("X-Auth-Token", GetResponseEmailMarketingConstants.GET_RESPONSE_ACCESS_TOKEN)
            .get(String.class);

	    // Criando um objeto ObjectMapper para realizar a conversão entre JSON e objetos Java
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    // Habilitando a configuração para aceitar um valor único como array ao desserializar JSON
	    objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

	    // Convertendo o JSON em uma lista de campos de remetentes
	    List<GetResponseEmailMarketingFromFieldDTO> camposRemetentes = objectMapper.readValue(
	        respostaJson, new TypeReference<List<GetResponseEmailMarketingFromFieldDTO>>() {}
	    );

	    return camposRemetentes;
	}
	
}
