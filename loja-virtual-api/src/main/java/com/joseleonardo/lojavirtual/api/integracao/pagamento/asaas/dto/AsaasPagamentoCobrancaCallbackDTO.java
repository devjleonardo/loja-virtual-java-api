package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto;

import java.io.Serializable;

public class AsaasPagamentoCobrancaCallbackDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * URL que o cliente será redirecionado após o pagamento com sucesso da fatura 
	 * ou link de pagamento
	 */
	private String successUrl;
	
	/*
	 * Definir se o cliente será redirecionado automaticamente ou será apenas informado com 
	 * um botão para retornar ao site. O padrão é true, caso queira desativar informar false
	 */
	private boolean autoRedirect = true;

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public boolean isAutoRedirect() {
		return autoRedirect;
	}

	public void setAutoRedirect(boolean autoRedirect) {
		this.autoRedirect = autoRedirect;
	}
	
}
