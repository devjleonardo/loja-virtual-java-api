package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto;

import java.io.Serializable;

public class AsaasPagamentoCobrancaSplitDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// Identificador da carteira Asaas que será transferido
	private String walletId;
	
	// Valor fixo a ser transferido para a conta quando a cobrança for recebida
	private float fixedValue;
	
	// Percentual sobre o valor líquido da cobrança a ser transferido quando for recebida
	private float percentualValue;
	
	/*
	 * (Somente parcelamentos). Valor que será feito split referente ao valor total que
	 * será parcelado.
	 */
	private float totalFixedValue;

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public float getFixedValue() {
		return fixedValue;
	}

	public void setFixedValue(float fixedValue) {
		this.fixedValue = fixedValue;
	}

	public float getPercentualValue() {
		return percentualValue;
	}

	public void setPercentualValue(float percentualValue) {
		this.percentualValue = percentualValue;
	}

	public float getTotalFixedValue() {
		return totalFixedValue;
	}

	public void setTotalFixedValue(float totalFixedValue) {
		this.totalFixedValue = totalFixedValue;
	}
	
}
