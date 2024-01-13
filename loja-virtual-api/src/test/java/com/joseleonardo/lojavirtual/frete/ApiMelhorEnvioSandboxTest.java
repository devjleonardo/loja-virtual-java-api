package com.joseleonardo.lojavirtual.frete;

import java.io.IOException;

import com.joseleonardo.lojavirtual.api.integracao.frete.melhor_envio.constants.MelhorEnvioFreteConstants;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiMelhorEnvioSandboxTest {

	public static void main(String[] args) throws IOException {
		/* Inserir fretes no carrinho */
		/*
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"from\":{\"name\":\"Nome do remetente\",\"phone\":\"53984470102\",\"email\":\"contato@melhorenvio.com.br\",\"document\":\"16571478358\",\"company_document\":\"89794131000100\",\"state_register\":\"123456\",\"address\":\"Endereço do remetente\",\"complement\":\"Complemento\",\"number\":\"1\",\"district\":\"Bairro\",\"city\":\"São Paulo\",\"country_id\":\"BR\",\"postal_code\":\"01002001\",\"state_abbr\":\"SP\",\"note\":\"observação\"},\"to\":{\"name\":\"Nome do destinatário\",\"phone\":\"53984470102\",\"email\":\"contato@melhorenvio.com.br\",\"document\":\"25404918047\",\"company_document\":\"07595604000177\",\"state_register\":\"123456\",\"address\":\"Endereço do destinatário\",\"complement\":\"Complemento\",\"number\":\"2\",\"district\":\"Bairro\",\"city\":\"Porto Alegre\",\"country_id\":\"BR\",\"postal_code\":\"90570020\",\"state_abbr\":\"RS\",\"note\":\"observação\"},\"options\":{\"receipt\":false,\"own_hand\":false,\"reverse\":false,\"non_commercial\":false,\"invoice\":{\"key\":\"31190307586261000184550010000092481404848162\"},\"insurance_value\":1000,\"plataform\":\"Nome da Plataforma\",\"tags\":[{\"tag\":\"Identificação do pedido na plataforma, exemplo: 1000007\",\"Url\":\"Link direto para o pedido na plataforma, se possível, caso contrário pode ser passado o valor null\"}]},\"service\":1,\"agency\":49,\"products\":[{\"name\":\"Papel adesivo para etiquetas 1\",\"quantity\":\"3\",\"unitary_value\":\"100.00\"},{\"name\":\"Papel adesivo para etiquetas 2\",\"quantity\":\"1\",\"unitary_value\":\"700.00\"}],\"volumes\":[{\"height\":15,\"width\":20,\"length\":10,\"weight\":3.5}]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDABOX + "/api/v2/me/cart")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
		
		System.out.println(response.body().string());
		*/
		
		
		/* Compra de fretes */
		/*
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"9b09a9fa-4d15-4cb6-a533-91dace560861\"]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDABOX + "/api/v2/me/shipment/checkout")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
		
		System.out.println(response.body().string());
		*/
		
		
		/* Geração de etiquetas */
		/*
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"9b09a9fa-4d15-4cb6-a533-91dace560861\"]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDABOX + "/api/v2/me/shipment/generate")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
		
		System.out.println(response.body().string());
		*/
		
		
		/* Impressão de etiquetas */
		/*
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\"mode\":\"private\",\"orders\":[\"9b09a9fa-4d15-4cb6-a533-91dace560861\"]}");
		Request request = new Request.Builder()
		  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SANDABOX + "/api/v2/me/shipment/print")
		  .post(body)
		  .addHeader("Accept", "application/json")
		  .addHeader("Content-Type", "application/json")
		  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SANDBOX)
		  .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		  .build();

		Response response = client.newCall(request).execute();
		
		System.out.println(response.body().string());
		*/
		
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		
		RequestBody body = RequestBody.create(
				mediaType, 
				"{\"orders\":[\"9b09a9fa-4d15-4cb6-a533-91dace560861\"]}"
		);
		
		Request request = new Request.Builder()
		    .url(MelhorEnvioFreteConstants.MELHOR_ENVIO_URL_SANDABOX + "/api/v2/me/shipment/tracking")
		    .post(body)
		    .addHeader("Accept", "application/json")
		    .addHeader("Content-type", "application/json")
		    .addHeader(
		        "Authorization", "Bearer " 
		        + MelhorEnvioFreteConstants.MELHOR_ENVIO_ACCESS_TOKEN_SANDBOX
		    )
		    .addHeader("User-Agent", "jlcb.lojavirtual@gmail.com")
		    .build();

		Response response = client.newCall(request).execute();
		
		System.out.println(response.body().string());
	}
	
}
