package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.cobrancas.listar_cobrancas;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.AsaasPagamentoDiscountDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.AsaasPagamentoFineDTO;
import com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.AsaasPagamentoInterestDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AsaasPagamentoListarCobrancasDataDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Tipo do objeto
	private String object;
	
	// Identificador único da cobrança no Asaas
	private String id;
	
    // Identificador único do cliente ao qual a cobrança pertence
    private String customer;

    // Data de criação da cobrança
    private String dateCreated;

    // Data de vencimento da cobrança
    private String dueDate;

    // Identificador único da assinatura (quando cobrança recorrente)
    private String installment;

    // Identificador único do parcelamento (quando cobrança parcelada)
    private String subscription;

    // Identificador único do link de pagamentos ao qual a cobrança pertence
    private String paymentLink;

    // Valor da cobrança
    private double value;

    // Valor líquido da cobrança após desconto da tarifa do Asaas
    private double netValue;

    // Forma de pagamento
    private String billingType; 
    
    // Data em que a cobrança foi confirmada
    private String confirmedDate;
    
    // Status da cobrança
    private String status; 

    // Descrição da cobrança
    private String description;

    // Campo livre para busca
    private String externalReference;

    // Informa se a cobrança pode ser paga após o vencimento (Somente para boleto)
    private boolean canBePaidAfterDueDate;

    // Identificador único da transação Pix à qual a cobrança pertence
    private String pixTransaction;

    // Identificador único do QrCode estático gerado para determinada chave Pix
    private String pixQrCodeId;

    // Valor original da cobrança (preenchido quando paga com juros e multa)
    private double originalValue;

    // Valor calculado de juros e multa que deve ser pago após o vencimento da cobrança
    private double interestValue;

    // Vencimento original no ato da criação da cobrança
    private String originalDueDate;

    // Data de liquidação da cobrança no Asaas
    private String paymentDate;

    // Data em que o cliente efetuou o pagamento do boleto
    private String clientPaymentDate;

    // Número da parcela
    private String installmentNumber;

    // URL do comprovante de confirmação, recebimento, estorno ou remoção
    private String transactionReceiptUrl;

    // Identificação única do boleto
    private String nossoNumero;

    // URL da fatura
    private String invoiceUrl;

    // URL para download do boleto
    private String bankSlipUrl;

    // Número da fatura
    private String invoiceNumber;
    
    // Informações de desconto
    private AsaasPagamentoDiscountDTO discount = new AsaasPagamentoDiscountDTO();

    // Informações de multa para pagamento após o vencimento
    private AsaasPagamentoFineDTO fine = new AsaasPagamentoFineDTO();

    // Informações de juros para pagamento após o vencimento
    private AsaasPagamentoInterestDTO interest = new AsaasPagamentoInterestDTO();
    
    // Determina se a cobrança foi removida
    private boolean deleted;
    
    // Define se a cobrança será enviada via Correios
	private boolean postalService = false;
	
	// Define se a cobrança foi antecipada ou está em processo de antecipação
	private boolean anticipated;
	
	// Determina se a cobrança é antecipável
	private boolean anticipable;
	
	// Data em que o crédito foi efetuado para a cobrança
	private String creditDate;

	// Data estimada para o crédito da cobrança
	private String estimatedCreditDate;
	
	// Data da última visualização da fatura
	private String lastInvoiceViewedDate;
	
	// Data da última visualização do boleto bancário
	private String lastBankSlipViewedDate;

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getInstallment() {
		return installment;
	}

	public void setInstallment(String installment) {
		this.installment = installment;
	}

	public String getSubscription() {
		return subscription;
	}

	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}

	public String getPaymentLink() {
		return paymentLink;
	}

	public void setPaymentLink(String paymentLink) {
		this.paymentLink = paymentLink;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getNetValue() {
		return netValue;
	}

	public void setNetValue(double netValue) {
		this.netValue = netValue;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(String confirmedDate) {
		this.confirmedDate = confirmedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public boolean isCanBePaidAfterDueDate() {
		return canBePaidAfterDueDate;
	}

	public void setCanBePaidAfterDueDate(boolean canBePaidAfterDueDate) {
		this.canBePaidAfterDueDate = canBePaidAfterDueDate;
	}

	public String getPixTransaction() {
		return pixTransaction;
	}

	public void setPixTransaction(String pixTransaction) {
		this.pixTransaction = pixTransaction;
	}

	public String getPixQrCodeId() {
		return pixQrCodeId;
	}

	public void setPixQrCodeId(String pixQrCodeId) {
		this.pixQrCodeId = pixQrCodeId;
	}

	public double getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(double originalValue) {
		this.originalValue = originalValue;
	}

	public double getInterestValue() {
		return interestValue;
	}

	public void setInterestValue(double interestValue) {
		this.interestValue = interestValue;
	}

	public String getOriginalDueDate() {
		return originalDueDate;
	}

	public void setOriginalDueDate(String originalDueDate) {
		this.originalDueDate = originalDueDate;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getClientPaymentDate() {
		return clientPaymentDate;
	}

	public void setClientPaymentDate(String clientPaymentDate) {
		this.clientPaymentDate = clientPaymentDate;
	}

	public String getInstallmentNumber() {
		return installmentNumber;
	}

	public void setInstallmentNumber(String installmentNumber) {
		this.installmentNumber = installmentNumber;
	}

	public String getTransactionReceiptUrl() {
		return transactionReceiptUrl;
	}

	public void setTransactionReceiptUrl(String transactionReceiptUrl) {
		this.transactionReceiptUrl = transactionReceiptUrl;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getInvoiceUrl() {
		return invoiceUrl;
	}

	public void setInvoiceUrl(String invoiceUrl) {
		this.invoiceUrl = invoiceUrl;
	}

	public String getBankSlipUrl() {
		return bankSlipUrl;
	}

	public void setBankSlipUrl(String bankSlipUrl) {
		this.bankSlipUrl = bankSlipUrl;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public AsaasPagamentoDiscountDTO getDiscount() {
		return discount;
	}

	public void setDiscount(AsaasPagamentoDiscountDTO discount) {
		this.discount = discount;
	}

	public AsaasPagamentoFineDTO getFine() {
		return fine;
	}

	public void setFine(AsaasPagamentoFineDTO fine) {
		this.fine = fine;
	}

	public AsaasPagamentoInterestDTO getInterest() {
		return interest;
	}

	public void setInterest(AsaasPagamentoInterestDTO interest) {
		this.interest = interest;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isPostalService() {
		return postalService;
	}

	public void setPostalService(boolean postalService) {
		this.postalService = postalService;
	}

	public boolean isAnticipated() {
		return anticipated;
	}

	public void setAnticipated(boolean anticipated) {
		this.anticipated = anticipated;
	}

	public boolean isAnticipable() {
		return anticipable;
	}

	public void setAnticipable(boolean anticipable) {
		this.anticipable = anticipable;
	}

	public String getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(String creditDate) {
		this.creditDate = creditDate;
	}

	public String getEstimatedCreditDate() {
		return estimatedCreditDate;
	}

	public void setEstimatedCreditDate(String estimatedCreditDate) {
		this.estimatedCreditDate = estimatedCreditDate;
	}

	public String getLastInvoiceViewedDate() {
		return lastInvoiceViewedDate;
	}

	public void setLastInvoiceViewedDate(String lastInvoiceViewedDate) {
		this.lastInvoiceViewedDate = lastInvoiceViewedDate;
	}

	public String getLastBankSlipViewedDate() {
		return lastBankSlipViewedDate;
	}

	public void setLastBankSlipViewedDate(String lastBankSlipViewedDate) {
		this.lastBankSlipViewedDate = lastBankSlipViewedDate;
	}
	
}
