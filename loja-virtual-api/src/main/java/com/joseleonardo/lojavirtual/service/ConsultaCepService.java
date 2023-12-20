package com.joseleonardo.lojavirtual.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.joseleonardo.lojavirtual.model.dto.CepDTO;

@Service
public class ConsultaCepService {
	
	public CepDTO consultarCep(String cep) {
		return new RestTemplate()
				.getForEntity("https://viacep.com.br/ws/" + cep  + "/json/", CepDTO.class)
				.getBody();
	}

}
