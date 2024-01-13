package com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.dto.contato;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.dto.campanha.GetResponseEmailMarketingCampaignDTO;

public class GetResponseEmailMarketingCriacaoContatoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	
	private GetResponseEmailMarketingCampaignDTO campaign = new GetResponseEmailMarketingCampaignDTO();
	
	private String email;
	
	private String dayOfCycle = "0";
	
	private String scoring;
	
	private List<String> tags = new ArrayList<>();
	
	private List<GetResponseEmailMarketingCustomFieldValuesDTO> customFieldValues = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GetResponseEmailMarketingCampaignDTO getCampaign() {
		return campaign;
	}

	public void setCampaign(GetResponseEmailMarketingCampaignDTO campaign) {
		this.campaign = campaign;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDayOfCycle() {
		return dayOfCycle;
	}

	public void setDayOfCycle(String dayOfCycle) {
		this.dayOfCycle = dayOfCycle;
	}

	public String getScoring() {
		return scoring;
	}

	public void setScoring(String scoring) {
		this.scoring = scoring;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<GetResponseEmailMarketingCustomFieldValuesDTO> getCustomFieldValues() {
		return customFieldValues;
	}

	public void setCustomFieldValues(List<GetResponseEmailMarketingCustomFieldValuesDTO> customFieldValues) {
		this.customFieldValues = customFieldValues;
	}
	
}
