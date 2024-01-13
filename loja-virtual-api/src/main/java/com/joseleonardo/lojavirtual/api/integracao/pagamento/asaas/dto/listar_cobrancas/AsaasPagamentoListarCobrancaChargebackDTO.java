package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.listar_cobrancas;

import java.io.Serializable;

public class AsaasPagamentoListarCobrancaChargebackDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Status do chargeback
	private String status;

	// Razão do chargeback
	private String reason;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
