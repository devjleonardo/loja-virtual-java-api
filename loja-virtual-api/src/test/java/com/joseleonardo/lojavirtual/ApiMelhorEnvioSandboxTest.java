package com.joseleonardo.lojavirtual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joseleonardo.lojavirtual.integracao.ApiTokenIntegracao;
import com.joseleonardo.lojavirtual.model.dto.frete.EmpresaTransporteDTO;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiMelhorEnvioSandboxTest {

	public static void main(String[] args) throws IOException {
		OkHttpClient client = new OkHttpClient(); /* Instancia o objeto de requisição */

		MediaType mediaType = MediaType.parse("application/json"); /* Tipo dos dados em JSON */
		RequestBody body = RequestBody.create(mediaType, "{\"from\":{\"postal_code\":\"96020360\"},\"to\":{\"postal_code\":\"01018020\"},\"products\":[{\"id\":\"x\",\"width\":11,\"height\":17,\"length\":11,\"weight\":0.3,\"insurance_value\":10.1,\"quantity\":1},{\"id\":\"y\",\"width\":16,\"height\":25,\"length\":11,\"weight\":0.3,\"insurance_value\":55.05,\"quantity\":2},{\"id\":\"z\",\"width\":22,\"height\":30,\"length\":11,\"weight\":1,\"insurance_value\":30,\"quantity\":1}]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDABOX + "/api/v2/me/shipment/calculate")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
		
		//System.out.println(response.body().string());
		
		JsonNode retornoDaApi = new ObjectMapper().readTree(response.body().string());
		
		Iterator<JsonNode> camposApi = retornoDaApi.iterator();
		
		List<EmpresaTransporteDTO> empresasTransporte = new ArrayList<>();
		
		while (camposApi.hasNext()) {
			JsonNode campoApi = camposApi.next();
			
			EmpresaTransporteDTO empresaTransporte = new EmpresaTransporteDTO();
			
			if (campoApi.get("id") != null) {
				empresaTransporte.setId(campoApi.get("id").toString());
			}
			
			if (campoApi.get("name") != null) {
				empresaTransporte.setNome(campoApi.get("name").asText());
			}
			
			if (campoApi.get("price") != null) {
				empresaTransporte.setValor(campoApi.get("price").asText());
			}
			
			if (campoApi.get("company") != null) {
				empresaTransporte.setEmpresa(campoApi.get("company").asText());
				empresaTransporte.setFoto(campoApi.get("company").get("picture").asText());
			}
			
			if(empresaTransporte.dadosOk()) {
				empresasTransporte.add(empresaTransporte);
			}
		}
		
		System.out.println(empresasTransporte);
	}
	
}
