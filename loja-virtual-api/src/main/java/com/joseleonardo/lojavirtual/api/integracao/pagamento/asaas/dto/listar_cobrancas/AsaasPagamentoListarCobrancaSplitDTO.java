package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.listar_cobrancas;

import java.io.Serializable;

public class AsaasPagamentoListarCobrancaSplitDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Identificador único do split
	private String id;
	
	// Identificador da carteira Asaas que será transferido
	private String walletId;
	
	// Valor fixo a ser transferido para a conta quando a cobrança for recebida
	private float fixedValue;
	
	// Percentual sobre o valor líquido da cobrança a ser transferido quando for recebida
	private float percentualValue;
	
	/*
	 * (Somente parcelamentos). Valor que será feito split referente ao 
	 * valor total que será parcelado.
	 */
	private float totalValue;
	
	// Motivo pela recusa do split
	private String refusalReason;
	
	// Status do split
	private String status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public float getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(float totalValue) {
		this.totalValue = totalValue;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
