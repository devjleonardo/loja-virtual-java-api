package com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.joseleonardo.lojavirtual.LojaVirtualApiApplication;
import com.joseleonardo.lojavirtual.api.integracao.email_marketing.get_response.dto.campanha.GetResponseEmailMarketingCampaignDTO;
import com.joseleonardo.lojavirtual.api.integracao.email_marketing.get_response.dto.campanha.GetResponseEmailMarketingCampaignListDTO;
import com.joseleonardo.lojavirtual.api.integracao.email_marketing.get_response.dto.contato.GetResponseEmailMarketingCriacaoContatoDTO;
import com.joseleonardo.lojavirtual.api.integracao.email_marketing.get_response.dto.fromfields.GetResponseEmailMarketingFromFieldDTO;
import com.joseleonardo.lojavirtual.api.integracao.email_marketing.get_response.service.GetResponseEmailMarketingService;

import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualApiApplication.class)
public class GetResponseEmailMarketingServiceTest extends TestCase {

	@Autowired
	private GetResponseEmailMarketingService getResponseEmailMarketingService;
	
	@Test
	public void testObterListaCampanhas() throws Exception {
		// Obtendo a lista de campanhas através do serviço de integração GetResponse
	    List<GetResponseEmailMarketingCampaignListDTO> campanhas =
	        getResponseEmailMarketingService.obterListaDeCampanhas();
	    
	    // Iterando sobre a lista de campanhas e imprimindo cada DTO
	    for (GetResponseEmailMarketingCampaignListDTO campanhaDTO : campanhas) {
	        // Imprimindo as informações da campanha no console
	        System.out.println(campanhaDTO);
	        // Imprimindo uma linha de separação no console
	        System.out.println("------------------------");
	    }
	}
	
	@Test
	public void testCriarNovoContato() throws Exception {
	    // Criando um DTO para a criação de um novo contato
	    GetResponseEmailMarketingCriacaoContatoDTO criacaoContatoDTO =
	        new GetResponseEmailMarketingCriacaoContatoDTO();

	    // Preenchendo dados do contato
	    criacaoContatoDTO.setName("José teste api");
	    criacaoContatoDTO.setEmail("leoprogamer1@gmail.com");

	    // Criando um DTO para a campanha
	    GetResponseEmailMarketingCampaignDTO campanhaDTO =
	        new GetResponseEmailMarketingCampaignDTO();

	    // Definindo o ID da campanha
	    campanhaDTO.setCampaignId("OVqrL");

	    // Associando a campanha ao DTO de criação de contato
	    criacaoContatoDTO.setCampaign(campanhaDTO);

	    // Chamando o método de criação de contato no serviço e obtendo o retorno da API
	    String retornoApi = getResponseEmailMarketingService.criarContato(criacaoContatoDTO);

	    // Imprimindo o retorno da API no console
	    System.out.println(retornoApi);
	}

	@Test
	public void testeEnviarEmail() throws Exception {
	    // Chamando o método para enviar um e-mail pela API
	    String retornoApi = getResponseEmailMarketingService.enviarEmail(
	        "OVqrL", "Teste de e-mail",
	        "<html>"
	        + "<body>Texto do e-mail</body>"
	        + "</html>"
	    );
	    
	    // Imprimindo o retorno da API no console
	    System.out.println(retornoApi);
	}
	
	@Test
	public void testObterListaCamposRemetentes() throws Exception {
		// Obtendo a lista de campos remetentes através do serviço de integração GetResponse
		List<GetResponseEmailMarketingFromFieldDTO> camposRemetentes = 
	        getResponseEmailMarketingService.obterListaCamposRemetentes();

	    // Iterando sobre a lista de campos remetentes e imprimindo cada DTO
	    for (GetResponseEmailMarketingFromFieldDTO campoRemetenteDTO : camposRemetentes) {
	        System.out.println(campoRemetenteDTO);
	    }
	}
	
}
