package com.joseleonardo.lojavirtual.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "asaas_status_pagamento_cartao_credito")
@SequenceGenerator(name = "seq_asaas_status_pagamento_cartao_credito", sequenceName = "seq_asaas_status_pagamento_cartao_credito", allocationSize = 1, initialValue = 1)
public class AsaasStatusPagamentoCartaoCredito extends AsaasPagamento {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_asaas_status_pagamento_cartao_credito")
	private Long id;

	private BigDecimal valorParcela;

	private Integer numeroParcela;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	public Integer getNumeroParcela() {
		return numeroParcela;
	}

	public void setNumeroParcela(Integer numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

}
