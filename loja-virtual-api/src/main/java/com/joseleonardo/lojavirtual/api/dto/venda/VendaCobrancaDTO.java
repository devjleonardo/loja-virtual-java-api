package com.joseleonardo.lojavirtual.api.dto.venda;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VendaCobrancaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String descricaoCobranca;

	private String nomeCliente;

	private String cpfCnpjCliente;

	private String emailCliente;

	private String telefoneCliente;

	@JsonProperty(value = "de" )
	private Long idVenda;

	public String getDescricaoCobranca() {
		return descricaoCobranca;
	}

	public void setDescricaoCobranca(String descricaoCobranca) {
		this.descricaoCobranca = descricaoCobranca;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getCpfCnpjCliente() {
		return cpfCnpjCliente;
	}

	public void setCpfCnpjCliente(String cpfCnpjCliente) {
		this.cpfCnpjCliente = cpfCnpjCliente;
	}

	public String getEmailCliente() {
		return emailCliente;
	}

	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}

	public String getTelefoneCliente() {
		return telefoneCliente;
	}

	public void setTelefoneCliente(String telefoneCliente) {
		this.telefoneCliente = telefoneCliente;
	}

	public Long getIdVenda() {
		return idVenda;
	}

	public void setIdVenda(Long idVenda) {
		this.idVenda = idVenda;
	}

}
