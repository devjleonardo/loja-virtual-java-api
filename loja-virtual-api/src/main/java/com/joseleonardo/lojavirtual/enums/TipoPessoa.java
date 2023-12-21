package com.joseleonardo.lojavirtual.enums;

public enum TipoPessoa {

	JURIDICA("Jurídica"),
	JURIDICA_FORNECEDOR("Jurídica Fornecedor"),
	FISICA("Física");

	String descricao;

	private TipoPessoa(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
