package com.joseleonardo.lojavirtual.model.dto.melhor.envio.criando.envios;

import java.io.Serializable;

public class TagsEnvioDTO implements Serializable {

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
