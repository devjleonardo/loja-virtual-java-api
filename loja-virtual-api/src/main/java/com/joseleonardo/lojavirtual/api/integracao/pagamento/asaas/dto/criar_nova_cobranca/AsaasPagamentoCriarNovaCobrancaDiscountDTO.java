package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.criar_nova_cobranca;

import java.io.Serializable;

public class AsaasPagamentoCriarNovaCobrancaDiscountDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Valor percentual ou fixo de desconto a ser aplicado sobre o valor da cobrança
	private float value;

	/*
	 * Dias antes do vencimento para aplicar desconto. Ex: 0 = até o vencimento, 1 =
	 * até um dia antes, 2 = até dois dias antes, e assim por diante
	 */
	private float dueDateLimitDays;

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public float getDueDateLimitDays() {
		return dueDateLimitDays;
	}

	public void setDueDateLimitDays(float dueDateLimitDays) {
		this.dueDateLimitDays = dueDateLimitDays;
	}

}
