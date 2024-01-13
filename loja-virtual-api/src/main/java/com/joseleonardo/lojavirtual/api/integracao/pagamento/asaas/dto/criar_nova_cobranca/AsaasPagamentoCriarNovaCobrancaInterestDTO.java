package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.criar_nova_cobranca;

import java.io.Serializable;

public class AsaasPagamentoCriarNovaCobrancaInterestDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Percentual de juros ao mês sobre o valor da cobrança para pagamento após o vencimento
	private float value;

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

}
