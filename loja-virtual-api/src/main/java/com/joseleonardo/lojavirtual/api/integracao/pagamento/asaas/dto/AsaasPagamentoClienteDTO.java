package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto;

import java.io.Serializable;

import com.joseleonardo.lojavirtual.util.ValidacaoCPF;

public class AsaasPagamentoClienteDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private String cpfCnpj;

	private String email;

	private String mobilePhone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		if (!ValidacaoCPF.isCPF(cpfCnpj)) {
			this.cpfCnpj = "07808516090";
		} else {
			this.cpfCnpj = cpfCnpj;
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

}
