package com.joseleonardo.lojavirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;

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

@Entity
@Table(name = "asaas_boleto_pix")
@SequenceGenerator(name = "seq_asaas_boleto_pix", sequenceName = "seq_asaas_boleto_pix", allocationSize = 1, initialValue = 1)
public class AsaasBoletoPix implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_asaas_boleto_pix")
	private Long id;

	private String idCobrancaAsaas;

	private String urlFatura;

	private boolean pago = false;

	private String dataVencimento;

	private BigDecimal valor = BigDecimal.ZERO;

	private Integer recorrencia = 0;

	@Column(columnDefinition = "text")
	private String pixQrCodeImagem;

	@Column(columnDefinition = "text")
	private String pixQrCodeCopiaCola;

	private String pixQrCodeExpiracao;

	@ManyToOne
	@JoinColumn(name = "venda_compra_loja_virtual_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_venda_compra_loja_virtual"))
	private VendaCompraLojaVirtual vendaCompraLojaVirtual;

	@ManyToOne(targetEntity = PessoaJuridica.class)
	@JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "fk_empresa"))
	private PessoaJuridica empresa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdCobrancaAsaas() {
		return idCobrancaAsaas;
	}

	public void setIdCobrancaAsaas(String idCobrancaAsaas) {
		this.idCobrancaAsaas = idCobrancaAsaas;
	}

	public String getUrlFatura() {
		return urlFatura;
	}

	public void setUrlFatura(String urlFatura) {
		this.urlFatura = urlFatura;
	}

	public boolean isPago() {
		return pago;
	}

	public void setPago(boolean pago) {
		this.pago = pago;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getRecorrencia() {
		return recorrencia;
	}

	public void setRecorrencia(Integer recorrencia) {
		this.recorrencia = recorrencia;
	}

	public VendaCompraLojaVirtual getVendaCompraLojaVirtual() {
		return vendaCompraLojaVirtual;
	}

	public void setVendaCompraLojaVirtual(VendaCompraLojaVirtual vendaCompraLojaVirtual) {
		this.vendaCompraLojaVirtual = vendaCompraLojaVirtual;
	}

	public PessoaJuridica getEmpresa() {
		return empresa;
	}

	public void setEmpresa(PessoaJuridica empresa) {
		this.empresa = empresa;
	}

	public String getPixQrCodeImagem() {
		return pixQrCodeImagem;
	}

	public void setPixQrCodeImagem(String pixQrCodeImagem) {
		this.pixQrCodeImagem = pixQrCodeImagem;
	}

	public String getPixQrCodeCopiaCola() {
		return pixQrCodeCopiaCola;
	}

	public void setPixQrCodeCopiaCola(String pixQrCodeCopiaCola) {
		this.pixQrCodeCopiaCola = pixQrCodeCopiaCola;
	}

	public String getPixQrCodeExpiracao() {
		return pixQrCodeExpiracao;
	}

	public void setPixQrCodeExpiracao(String pixQrCodeExpiracao) {
		this.pixQrCodeExpiracao = pixQrCodeExpiracao;
	}

}
