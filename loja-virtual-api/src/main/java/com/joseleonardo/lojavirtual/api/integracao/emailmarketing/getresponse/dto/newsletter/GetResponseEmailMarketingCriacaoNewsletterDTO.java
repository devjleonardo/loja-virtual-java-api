package com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.dto.newsletter;

import java.io.Serializable;
import java.util.ArrayList;

import com.joseleonardo.lojavirtual.api.integracao.emailmarketing.getresponse.dto.campanha.GetResponseEmailMarketingCampaignDTO;

public class GetResponseEmailMarketingCriacaoNewsletterDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/* O conteúdo da mensagem do e-mail HTML ou texto */
	private GetResponseEmailMarketingMessageContentDTO content = new GetResponseEmailMarketingMessageContentDTO();;
	
	private ArrayList<String> flags = new ArrayList<String>();
	
	/* Nome do e-mail (no máximo 128 letras) */
	private String name;
	
	/* Tipo do e-mail transmissão (broadcast) ou rascunho (draft) */
	private String type = "broadcast";
	
	/* Editor utilizado para criar o conteúdo da mensagem */
	private String editor = "custom";
	
	/* Assunto da mensagem do e-mail */
	private String subject;
	
	/* 'From' Endereço de e-mail usado para a mensagem */
	private GetResponseEmailMarketingFromFieldReferenceDTO fromField = new GetResponseEmailMarketingFromFieldReferenceDTO();
	
	/* Endereço de e-mail utilizado para resposta */
	private GetResponseEmailMarketingFromFieldReferenceDTO replyTo = new GetResponseEmailMarketingFromFieldReferenceDTO();
	
	/* Campanha na qual o e-mail é atribuída */
	private GetResponseEmailMarketingCampaignDTO campaign = new GetResponseEmailMarketingCampaignDTO();
	
	/* Data de envio 2024-05-12T18:20:52-03:00 */
	private String sendOn;
	
	/* Os anexos e arquivos do e-mail caso queria enviar */
	private ArrayList<GetResponseEmailMarketingAttachmentsDTO> attachments = new ArrayList<>();
	
    /* Configurações extras de envio */
	private GetResponseEmailMarketingSendSettingsDTO sendSettings = new GetResponseEmailMarketingSendSettingsDTO();

	public GetResponseEmailMarketingMessageContentDTO getContent() {
		return content;
	}

	public void setContent(GetResponseEmailMarketingMessageContentDTO content) {
		this.content = content;
	}

	public ArrayList<String> getFlags() {
		return flags;
	}
	
	public void setFlags(ArrayList<String> flags) {
		this.flags = flags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public GetResponseEmailMarketingFromFieldReferenceDTO getFromField() {
		return fromField;
	}

	public void setFromField(GetResponseEmailMarketingFromFieldReferenceDTO fromField) {
		this.fromField = fromField;
	}

	public GetResponseEmailMarketingFromFieldReferenceDTO getReplyTo() {
		return replyTo;
	}
	
	public void setReplyTo(GetResponseEmailMarketingFromFieldReferenceDTO replyTo) {
		this.replyTo = replyTo;
	}

	public GetResponseEmailMarketingCampaignDTO getCampaign() {
		return campaign;
	}

	public void setCampaign(GetResponseEmailMarketingCampaignDTO campaign) {
		this.campaign = campaign;
	}

	public String getSendOn() {
		return sendOn;
	}

	public void setSendOn(String sendOn) {
		this.sendOn = sendOn;
	}

	public ArrayList<GetResponseEmailMarketingAttachmentsDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<GetResponseEmailMarketingAttachmentsDTO> attachments) {
		this.attachments = attachments;
	}

	public GetResponseEmailMarketingSendSettingsDTO getSendSettings() {
		return sendSettings;
	}

	public void setSendSettings(GetResponseEmailMarketingSendSettingsDTO sendSettings) {
		this.sendSettings = sendSettings;
	}

}
