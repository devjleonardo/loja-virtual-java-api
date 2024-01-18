package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AsaasPagamentoErrorListResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<AsaasPagamentoErrorResponseDTO> errors = new ArrayList<>();
	
	public String listarErrors() {
		StringBuilder stringBuilder = new StringBuilder();
		
		for (AsaasPagamentoErrorResponseDTO error : errors) {
			stringBuilder.append(error.getDescription())
			    .append(" - Code: ")
			    .append(error.getCode())
			    .append("\n");
		}
		
		return stringBuilder.toString();
	}

	public List<AsaasPagamentoErrorResponseDTO> getErrors() {
		return errors;
	}

	public void setErrors(List<AsaasPagamentoErrorResponseDTO> errors) {
		this.errors = errors;
	}
	
}
