package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.cobrancas.obter_qr_code_pix;

import java.io.Serializable;

public class AsaasPagamentoPixQRCodeDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Imagem do QrCode em base64
	private String encodedImage;

	// Copia e Cola do QrCode
	private String payload;

	// Data de expiração do QrCode
	private String expirationDate;

	public String getEncodedImage() {
		return encodedImage;
	}

	public void setEncodedImage(String encodedImage) {
		this.encodedImage = encodedImage;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

}
