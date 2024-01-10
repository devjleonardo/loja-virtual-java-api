package com.joseleonardo.lojavirtual.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.joseleonardo.lojavirtual.api.consulta.cnpj.dto.CnpjDTO;

@Service
public class ConsultaCnpjService {
	
	public CnpjDTO consultarCnpjReceitaWS(String cnpj) {
		return new RestTemplate()
				.getForEntity("https://receitaws.com.br/v1/cnpj/" + cnpj, CnpjDTO.class)
				.getBody();
	}

}
