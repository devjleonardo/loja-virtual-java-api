package com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.dto.newsletter;

import java.io.Serializable;

public class GetResponseEmailMarketingFromFieldReferenceDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fromFieldId;

	public String getFromFieldId() {
		return fromFieldId;
	}

	public void setFromFieldId(String fromFieldId) {
		this.fromFieldId = fromFieldId;
	}

}
