package com.joseleonardo.lojavirtual.api.dto.relatorio;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RelatorioVendaCompraLojaVirtualPorStatusDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Informe a data da venda inicial")
	@NotNull(message = "Informe a data de venda inicial")
	private String dataVendaInicial;

	@NotBlank(message = "Informe a data de venda final")
	@NotNull(message = "Informe a data de venda final")
	private String dataVendaFinal;

	private String codigoProduto;
	private String nomeProduto;
	private String emailCliente;
	private String telefoneCliente;
	private String valorVendaProduto;
	private String codigoCliente;
	private String nomeCliente;
	private String quantidadeEstoque;
	private String codigoVenda;
	private String statusVendaCompraLojaVirtual;

	public String getDataVendaInicial() {
		return dataVendaInicial;
	}

	public void setDataVendaInicial(String dataVendaInicial) {
		this.dataVendaInicial = dataVendaInicial;
	}

	public String getDataVendaFinal() {
		return dataVendaFinal;
	}

	public void setDataVendaFinal(String dataVendaFinal) {
		this.dataVendaFinal = dataVendaFinal;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
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

	public String getValorVendaProduto() {
		return valorVendaProduto;
	}

	public void setValorVendaProduto(String valorVendaProduto) {
		this.valorVendaProduto = valorVendaProduto;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(String quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public String getCodigoVenda() {
		return codigoVenda;
	}

	public void setCodigoVenda(String codigoVenda) {
		this.codigoVenda = codigoVenda;
	}

	public String getStatusVendaCompraLojaVirtual() {
		return statusVendaCompraLojaVirtual;
	}
	
	public void setStatusVendaCompraLojaVirtual(String statusVendaCompraLojaVirtual) {
		this.statusVendaCompraLojaVirtual = statusVendaCompraLojaVirtual;
	}
	
}
