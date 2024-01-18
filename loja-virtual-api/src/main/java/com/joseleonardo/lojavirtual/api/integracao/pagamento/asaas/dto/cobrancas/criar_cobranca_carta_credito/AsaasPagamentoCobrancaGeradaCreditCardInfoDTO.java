package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.cobrancas.criar_cobranca_carta_credito;

import java.io.Serializable;

public class AsaasPagamentoCobrancaGeradaCreditCardInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Número do cartão de crédito utilizado na transação
	private String creditCardNumber;

	// Bandeira do cartão de crédito utilizado na transação
	private String creditCardBrand;

	// Token associado ao cartão de crédito utilizado na transação
	private String creditCardToken;

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public String getCreditCardBrand() {
		return creditCardBrand;
	}

	public void setCreditCardBrand(String creditCardBrand) {
		this.creditCardBrand = creditCardBrand;
	}

	public String getCreditCardToken() {
		return creditCardToken;
	}

	public void setCreditCardToken(String creditCardToken) {
		this.creditCardToken = creditCardToken;
	}

}
