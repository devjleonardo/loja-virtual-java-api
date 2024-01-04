package com.joseleonardo.lojavirtual.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RelatorioNotaFiscalCompraProdutosCompradosDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nomeProduto;
	
	@NotBlank(message = "Informe a data inicial")
	@NotNull(message = "Informe a data inicial")
	private String dataInicial;
	
	@NotBlank(message = "Informe a data final")
	@NotNull(message = "Informe a data final")
	private String dataFinal;
	
	private String codigoNotaFiscalCompra;
	private String codigoProduto;
	private String valorVendaProduto;
	private String quantidadeComprada;
	private String codigoFornecedor;
	private String nomeFornecedor;
	private String dataCompra;

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getCodigoNotaFiscalCompra() {
		return codigoNotaFiscalCompra;
	}

	public void setCodigoNotaFiscalCompra(String codigoNotaFiscalCompra) {
		this.codigoNotaFiscalCompra = codigoNotaFiscalCompra;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getValorVendaProduto() {
		return valorVendaProduto;
	}

	public void setValorVendaProduto(String valorVendaProduto) {
		this.valorVendaProduto = valorVendaProduto;
	}

	public String getQuantidadeComprada() {
		return quantidadeComprada;
	}

	public void setQuantidadeComprada(String quantidadeComprada) {
		this.quantidadeComprada = quantidadeComprada;
	}

	public String getCodigoFornecedor() {
		return codigoFornecedor;
	}

	public void setCodigoFornecedor(String codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public String getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(String dataCompra) {
		this.dataCompra = dataCompra;
	}

}
