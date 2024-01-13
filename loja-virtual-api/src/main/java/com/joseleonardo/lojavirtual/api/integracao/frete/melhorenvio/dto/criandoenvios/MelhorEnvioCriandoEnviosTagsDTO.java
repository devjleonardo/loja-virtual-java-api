package com.joseleonardo.lojavirtual.api.integracao.frete.melhorenvio.dto.criandoenvios;

import java.io.Serializable;

public class MelhorEnvioCriandoEnviosTagsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tag;
	
	private String Url;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

}
