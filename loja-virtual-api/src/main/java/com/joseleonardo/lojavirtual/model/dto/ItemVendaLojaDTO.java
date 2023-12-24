package com.joseleonardo.lojavirtual.model.dto;

import java.io.Serializable;

import com.joseleonardo.lojavirtual.model.Produto;

public class ItemVendaLojaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Double quantidade;

	private Produto produto;

	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

}
