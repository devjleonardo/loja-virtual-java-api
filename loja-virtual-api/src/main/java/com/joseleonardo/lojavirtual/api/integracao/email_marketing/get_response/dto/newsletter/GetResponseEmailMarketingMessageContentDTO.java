package com.joseleonardo.lojavirtual.api.integracao.email_marketing.get_response.dto.newsletter;

import java.io.Serializable;

public class GetResponseEmailMarketingMessageContentDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String html;
	
	private String plain;

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getPlain() {
		return plain;
	}

	public void setPlain(String plain) {
		this.plain = plain;
	}

}
