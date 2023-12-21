package com.joseleonardo.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.lojavirtual.model.dto.cnpj.CnpjDTO;
import com.joseleonardo.lojavirtual.service.ConsultaCnpjService;

@RestController
public class CnpjController {
	
	@Autowired
	private ConsultaCnpjService consultaCnpjService;
	
	@ResponseBody	
	@GetMapping(value =  "**/consultarCnpjReceitaWS/{cnpj}")
	public ResponseEntity<CnpjDTO> consultarCnpjReceitaWS(@PathVariable("cnpj") String cnpj) {
		CnpjDTO cnpjDTO  = consultaCnpjService.consultarCnpjReceitaWS(cnpj);

		return new ResponseEntity<CnpjDTO>(cnpjDTO, HttpStatus.OK);
	}

}
