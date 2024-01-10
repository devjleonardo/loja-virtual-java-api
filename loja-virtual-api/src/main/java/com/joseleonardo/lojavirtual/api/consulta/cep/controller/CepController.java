package com.joseleonardo.lojavirtual.api.consulta.cep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.lojavirtual.api.consulta.cep.dto.CepDTO;
import com.joseleonardo.lojavirtual.service.ConsultaCepService;

@RestController
public class CepController {

	@Autowired
	private ConsultaCepService consultaCepService;

	@ResponseBody
	@GetMapping(value =  "**/consultarCep/{cep}")
	public ResponseEntity<CepDTO> consultarCep(@PathVariable("cep") String cep) {
		CepDTO cepDTO  = consultaCepService.consultarCep(cep);

		return new ResponseEntity<CepDTO>(cepDTO, HttpStatus.OK);
	}
	
}
