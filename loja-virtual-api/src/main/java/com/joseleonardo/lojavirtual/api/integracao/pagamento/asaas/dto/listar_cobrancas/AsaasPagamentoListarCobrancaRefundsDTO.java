package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.listar_cobrancas;

import java.io.Serializable;

public class AsaasPagamentoListarCobrancaRefundsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Data da criação do estorno
	private String dateCreated;

	// Status do estorno
	private String status;

	// Valor do estorno
	private float value;

	// Descrição do estorno
	private String description;

	// Link do recibo da transação
	private String transactionReceiptUrl;

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTransactionReceiptUrl() {
		return transactionReceiptUrl;
	}

	public void setTransactionReceiptUrl(String transactionReceiptUrl) {
		this.transactionReceiptUrl = transactionReceiptUrl;
	}

}
