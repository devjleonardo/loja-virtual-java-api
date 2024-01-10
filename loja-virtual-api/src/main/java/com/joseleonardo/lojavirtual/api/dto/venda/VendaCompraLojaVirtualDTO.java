package com.joseleonardo.lojavirtual.api.dto.venda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.joseleonardo.lojavirtual.model.Endereco;
import com.joseleonardo.lojavirtual.model.Pessoa;

public class VendaCompraLojaVirtualDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private BigDecimal valorTotal;

	private BigDecimal valorDesconto;

	private BigDecimal valorFrete;

	private Pessoa pessoa;

	private Endereco enderecoEntrega;

	private Endereco enderecoCobranca;
	
	private List<ItemVendaLojaDTO> itensVendaLojaDTO = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Endereco getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public void setEnderecoEntrega(Endereco enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	public Endereco getEnderecoCobranca() {
		return enderecoCobranca;
	}

	public void setEnderecoCobranca(Endereco enderecoCobranca) {
		this.enderecoCobranca = enderecoCobranca;
	}

	public List<ItemVendaLojaDTO> getItensVendaLojaDTO() {
		return itensVendaLojaDTO;
	}
	
	public void setItensVendaLojaDTO(List<ItemVendaLojaDTO> itensVendaLojaDTO) {
		this.itensVendaLojaDTO = itensVendaLojaDTO;
	}
	
}
