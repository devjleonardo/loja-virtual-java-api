package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.cobrancas.criar_cobranca_carta_credito;

import java.io.Serializable;

public class AsaasPagamentoTitularCartaoCreditoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Nome do titular do cartão
	private String name;
	
	// Email do titular do cartão
	private String email;
	
	// CPF ou CNPJ do titular do cartão
	private String cpfCnpj;
	
	// CEP do titular do cartão
	private String postalCode;
	
	// Número do endereço do titular do cartão
	private String addressNumber;
	
	// Complemento do endereço do titular do cartão
	private String addressComplement;
	
	// Fone com DDD do titular do cartão
	private String phone;
	
	// Fone celular do titular do cartão
	private String mobilePhone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getAddressNumber() {
		return addressNumber;
	}

	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}

	public String getAddressComplement() {
		return addressComplement;
	}

	public void setAddressComplement(String addressComplement) {
		this.addressComplement = addressComplement;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
}
