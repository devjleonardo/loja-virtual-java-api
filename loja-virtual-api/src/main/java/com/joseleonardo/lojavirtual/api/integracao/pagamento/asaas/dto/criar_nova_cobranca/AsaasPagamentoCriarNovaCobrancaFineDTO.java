package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.criar_nova_cobranca;

import java.io.Serializable;

public class AsaasPagamentoCriarNovaCobrancaFineDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Percentual de multa sobre o valor da cobrança para pagamento após o vencimento
	private float value;

	private String type;

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
