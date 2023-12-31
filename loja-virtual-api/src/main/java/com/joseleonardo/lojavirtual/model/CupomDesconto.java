package com.joseleonardo.lojavirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cupom_desconto")
@SequenceGenerator(name = "seq_cupom_desconto", sequenceName = "seq_cupom_desconto", allocationSize = 1, initialValue = 1)
public class CupomDesconto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cupom_desconto")
	private Long id;

	@NotBlank(message = "O código/descrição do cupom de desconto deve ser informado")
	@NotNull(message = "O código/descrição do cupom de desconto deve ser informado")
	@Column(nullable = false)
	private String codigoDescricao;

	private BigDecimal valorEmReais;

	private BigDecimal valorEmPorcentagem;

	@NotNull(message = "A data de validade do cupom de desconto deve ser informada")
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataValidade;
	
	@ManyToOne(targetEntity = PessoaJuridica.class)
	@JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_empresa"))
	private PessoaJuridica empresa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoDescricao() {
		return codigoDescricao;
	}

	public void setCodigoDescricao(String codigoDescricao) {
		this.codigoDescricao = codigoDescricao;
	}

	public BigDecimal getValorEmReais() {
		return valorEmReais;
	}

	public void setValorEmReais(BigDecimal valorEmReais) {
		this.valorEmReais = valorEmReais;
	}

	public BigDecimal getValorEmPorcentagem() {
		return valorEmPorcentagem;
	}

	public void setValorEmPorcentagem(BigDecimal valorEmPorcentagem) {
		this.valorEmPorcentagem = valorEmPorcentagem;
	}

	public Date getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}
	
	public PessoaJuridica getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(PessoaJuridica empresa) {
		this.empresa = empresa;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CupomDesconto other = (CupomDesconto) obj;
		return Objects.equals(id, other.id);
	}

}
