package com.joseleonardo.lojavirtual.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pessoa_juridica")
@PrimaryKeyJoinColumn(name = "id")
public class PessoaJuridica extends Pessoa {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "O CNPJ deve ser informado")
	@NotNull(message = "O CNPJ deve ser informado")
	@Column(nullable = false)
	private String cnpj;

	@NotBlank(message = "A inscrição estadual deve ser informada")
	@NotNull(message = "A inscrição estadual deve ser informada")
	@Column(nullable = false)
	private String inscricaoEstadual;

	private String inscricaoMunicipal;

	@NotBlank(message = "O nome fantasia deve ser informado")
	@NotNull(message = "O nome fantasia deve ser informado")
	@Column(nullable = false)
	private String nomeFantasia;

	@NotBlank(message = "A razão social deve ser informada")
	@NotNull(message = "A razão social deve ser informada")
	@Column(nullable = false)
	private String razaoSocial;

	private String categoria;

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	@JsonIgnore
	@Override
	public PessoaJuridica getEmpresa() {
		return super.getEmpresa();
	}

}
