package com.joseleonardo.lojavirtual;

import com.joseleonardo.lojavirtual.util.ValidacaoCPF;

public class CPFTest {

	public static void main(String[] args) {
		if (ValidacaoCPF.isCPF("977.208.010-91")) {
			System.out.println("O CPF 977.208.010-91 é válido");
		}
		
		System.out.println("");
		
		if (!ValidacaoCPF.isCPF("312323")) {
			System.out.println("O CPF 4123213123 é inválido");
		}
	}
	
}
