package com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.dto.fromfields;

import java.io.Serializable;

public class GetResponseEmailMarketingFromFieldDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fromFieldId;

	private String href;

	private String email;

	private String name;

	private String isActive;

	private String isDefault;

	private String createdOn;

	public String getFromFieldId() {
		return fromFieldId;
	}

	public void setFromFieldId(String fromFieldId) {
		this.fromFieldId = fromFieldId;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

}
