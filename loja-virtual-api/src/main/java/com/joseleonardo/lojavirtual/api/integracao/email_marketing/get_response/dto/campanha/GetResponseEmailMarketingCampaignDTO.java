package com.joseleonardo.lojavirtual.api.integracao.email_marketing.get_response.dto.campanha;

import java.io.Serializable;

public class GetResponseEmailMarketingCampaignDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String campaignId;

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

}
