package com.joseleonardo.lojavirtual.model.enums;

public enum StatusVendaCompraLojaVirtual {

	FINALIZADA("Finalizada"),
	CANCELADA("Cancelada"),
	ABANDONOU_CARRINHO("Abandonou carrinho");
	
	private String descricao;

	private StatusVendaCompraLojaVirtual(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	@Override
	public String toString() {
		return descricao;
	}
	
}
