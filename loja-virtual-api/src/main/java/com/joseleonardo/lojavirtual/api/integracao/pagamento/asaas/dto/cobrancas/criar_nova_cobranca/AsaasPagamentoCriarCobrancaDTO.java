package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.cobrancas.criar_nova_cobranca;

import java.io.Serializable;

import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.AsaasPagamentoFineDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.AsaasPagamentoInterestDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.AsaasPagamentoDiscountDTO;

public class AsaasPagamentoCriarCobrancaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Identificador único do cliente no Asaas
	private String customer;

	// Forma de pagamento
	private String billingType;

	// Valor da cobrança
	private float value;
	
	// Data de vencimento da cobrança
	private String dueDate;
	
	// Descrição da cobrança (máx. 500 caracteres)
	private String description;
	
	// Dias após o vencimento para cancelamento do registro (somente para boleto bancário)
	private String daysAfterDueDateToCancellationRegistration;
	
	// Campo livre para busca
	private String externalReference;
	
	// Número de parcelas (somente no caso de cobrança parcelada)
	private int installmentCount;
	
	/*
	 * Informe o valor total de uma cobrança que será parcelada 
	 * (somente no caso de cobrança parcelada). Caso enviado este campo o installmentValue 
	 * não é necessário, o cálculo por parcela será automático.
	 */
	private String totalValue;
	
	/*
	 * Valor de cada parcela (somente no caso de cobrança parcelada).
	 * Envie este campo em caso de querer definir o valor de cada parcela.
	 */
	private float installmentValue;
	
	// Informações de desconto
	private AsaasPagamentoDiscountDTO discount = new AsaasPagamentoDiscountDTO();
	
	// Informações de juros para pagamento após o vencimento
	private AsaasPagamentoInterestDTO interest = new AsaasPagamentoInterestDTO();

	// Informações de multa para pagamento após o vencimento
	private AsaasPagamentoFineDTO fine = new AsaasPagamentoFineDTO();
	
	// Define se a cobrança será enviada via Correios
	private boolean postalService = false;

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDaysAfterDueDateToCancellationRegistration() {
		return daysAfterDueDateToCancellationRegistration;
	}

	public void setDaysAfterDueDateToCancellationRegistration(String daysAfterDueDateToCancellationRegistration) {
		this.daysAfterDueDateToCancellationRegistration = daysAfterDueDateToCancellationRegistration;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public int getInstallmentCount() {
		return installmentCount;
	}
	
	public void setInstallmentCount(int installmentCount) {
		this.installmentCount = installmentCount;
	}

	public String getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	public float getInstallmentValue() {
		return installmentValue;
	}
	
	public void setInstallmentValue(float installmentValue) {
		this.installmentValue = installmentValue;
	}

	public AsaasPagamentoDiscountDTO getDiscount() {
		return discount;
	}

	public void setDiscount(AsaasPagamentoDiscountDTO discount) {
		this.discount = discount;
	}

	public AsaasPagamentoInterestDTO getInterest() {
		return interest;
	}

	public void setInterest(AsaasPagamentoInterestDTO interest) {
		this.interest = interest;
	}

	public AsaasPagamentoFineDTO getFine() {
		return fine;
	}

	public void setFine(AsaasPagamentoFineDTO fine) {
		this.fine = fine;
	}

	public boolean isPostalService() {
		return postalService;
	}

	public void setPostalService(boolean postalService) {
		this.postalService = postalService;
	}
	
}
