package com.joseleonardo.lojavirtual.api.integracao.frete.melhorenvio.dto.cotacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MelhorEnvioCalculoFreteDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private MelhorEnvioCotacaoFromDTO from;

	private MelhorEnvioCotacaoToDTO to;

	private List<MelhorEnvioCotacaoProductsDTO> products = new ArrayList<>();

	public MelhorEnvioCotacaoFromDTO getFrom() {
		return from;
	}

	public void setFrom(MelhorEnvioCotacaoFromDTO from) {
		this.from = from;
	}

	public MelhorEnvioCotacaoToDTO getTo() {
		return to;
	}

	public void setTo(MelhorEnvioCotacaoToDTO to) {
		this.to = to;
	}

	public List<MelhorEnvioCotacaoProductsDTO> getProducts() {
		return products;
	}

	public void setProducts(List<MelhorEnvioCotacaoProductsDTO> products) {
		this.products = products;
	}

}
