package com.joseleonardo.lojavirtual.api.integracao.frete.melhorenvio.dto.criandoenvios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InsercaoFretesNoCarrinhoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String service;

	private String agency;

	private FromEnvioDTO from = new FromEnvioDTO();

	private ToEnvioDTO to = new ToEnvioDTO();

	private List<ProductsEnvioDTO> products = new ArrayList<>();

	private List<VolumesEnvioDTO> volumes = new ArrayList<>();

	private OptionsEnvioDTO options = new OptionsEnvioDTO();

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public FromEnvioDTO getFrom() {
		return from;
	}

	public void setFrom(FromEnvioDTO from) {
		this.from = from;
	}

	public ToEnvioDTO getTo() {
		return to;
	}

	public void setTo(ToEnvioDTO to) {
		this.to = to;
	}

	public List<ProductsEnvioDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductsEnvioDTO> products) {
		this.products = products;
	}

	public List<VolumesEnvioDTO> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<VolumesEnvioDTO> volumes) {
		this.volumes = volumes;
	}

	public OptionsEnvioDTO getOptions() {
		return options;
	}

	public void setOptions(OptionsEnvioDTO options) {
		this.options = options;
	}

}
