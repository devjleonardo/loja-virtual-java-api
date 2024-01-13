package com.joseleonardo.lojavirtual.api.integracao.frete.melhor_envio.dto.criandoenvios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MelhorEnvioInsercaoFretesCarrinhoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String service;

	private String agency;

	private MelhorEnvioCriandoEnviosFromDTO from = new MelhorEnvioCriandoEnviosFromDTO();

	private MelhorEnvioCriandoEnviosToDTO to = new MelhorEnvioCriandoEnviosToDTO();

	private List<MelhorEnvioCriandoEnviosProductsDTO> products = new ArrayList<>();

	private List<MelhorEnvioCriandoEnviosVolumesDTO> volumes = new ArrayList<>();

	private MelhorEnvioCriandoEnviosOptionsDTO options = new MelhorEnvioCriandoEnviosOptionsDTO();

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

	public MelhorEnvioCriandoEnviosFromDTO getFrom() {
		return from;
	}

	public void setFrom(MelhorEnvioCriandoEnviosFromDTO from) {
		this.from = from;
	}

	public MelhorEnvioCriandoEnviosToDTO getTo() {
		return to;
	}

	public void setTo(MelhorEnvioCriandoEnviosToDTO to) {
		this.to = to;
	}

	public List<MelhorEnvioCriandoEnviosProductsDTO> getProducts() {
		return products;
	}

	public void setProducts(List<MelhorEnvioCriandoEnviosProductsDTO> products) {
		this.products = products;
	}

	public List<MelhorEnvioCriandoEnviosVolumesDTO> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<MelhorEnvioCriandoEnviosVolumesDTO> volumes) {
		this.volumes = volumes;
	}

	public MelhorEnvioCriandoEnviosOptionsDTO getOptions() {
		return options;
	}

	public void setOptions(MelhorEnvioCriandoEnviosOptionsDTO options) {
		this.options = options;
	}

}
