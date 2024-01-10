package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.service;

import org.springframework.stereotype.Service;

import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.constants.AsaasPagamentoConstants;
import com.joseleonardo.lojavirtual.ssl.HostIgnoringSSLClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Service
public class AsaasPagamentoService {

	/**
	 * Criar a chave Pix dentro da nossa conta Asaas.
	 * 
	 * @return Chave
	 * @throws Exception
	 */
	public String criarChavePix() throws Exception {
		Client client = new HostIgnoringSSLClient(AsaasPagamentoConstants.URL_SANDBOX)
                .createUnsecuredHttpClient();
		
		WebResource webResource = client.resource(AsaasPagamentoConstants.URL_SANDBOX + "pix/addressKeys");
		
		ClientResponse clientResponse = webResource
			    .accept("application/json;charset=UTF-8")
			    .header("Content-Type", "application/json")
			    .header("access_token", AsaasPagamentoConstants.ACCESS_TOKEN_SANDBOX)
			    .post(ClientResponse.class, "{\"type\":\"EVP\"}");
		
		String stringRetorno = clientResponse.getEntity(String.class);
		
		clientResponse.close();
		
		return stringRetorno;
	}

}