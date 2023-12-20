package com.joseleonardo.lojavirtual;

import com.joseleonardo.lojavirtual.util.ValidacaoCNPJ;

public class CNPJTest {

	public static void main(String[] args) {
		if (ValidacaoCNPJ.isCNPJ("34.690.791/0001-40")) {
			System.out.println("O CNPJ 34.690.791/0001-40 é válido");
		}
		
		System.out.println("");
		
		if (!ValidacaoCNPJ.isCNPJ("4123213123")) {
			System.out.println("O CNPJ 4123213123 é inválido");
		}
	}
	
}
