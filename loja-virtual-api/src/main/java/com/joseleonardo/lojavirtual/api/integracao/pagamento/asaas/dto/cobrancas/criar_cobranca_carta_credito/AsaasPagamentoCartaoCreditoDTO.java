package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.cobrancas.criar_cobranca_carta_credito;

import java.io.Serializable;

public class AsaasPagamentoCartaoCreditoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Nome impresso no cartão
	private String holderName;

	// Número do cartão
	private String number;

	// Mês de expiração (ex: 06)
	private String expiryMonth;

	// Ano de expiração com 4 dígitos (ex: 2019)
	private String expiryYear;

	// Código de segurança
	private String ccv;

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExpiryMonth() {
		return expiryMonth;
	}

	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}

	public String getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}

	public String getCcv() {
		return ccv;
	}

	public void setCcv(String ccv) {
		this.ccv = ccv;
	}

}
