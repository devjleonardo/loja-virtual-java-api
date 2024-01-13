package com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.dto.contato;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetResponseEmailMarketingCustomFieldValuesDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String customFieldId;
	private List<String> value = new ArrayList<>();

	public String getCustomFieldId() {
		return customFieldId;
	}

	public void setCustomFieldId(String customFieldId) {
		this.customFieldId = customFieldId;
	}

	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> value) {
		this.value = value;
	}

}
