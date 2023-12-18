package com.joseleonardo.lojavirtual.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.lojavirtual.model.Acesso;
import com.joseleonardo.lojavirtual.service.AcessoService;

@RestController
public class AcessoController {

	@Autowired
	private AcessoService acessoService;

	@PostMapping("/salvarAcesso")
	public Acesso salvarAcesso(Acesso acesso) {
		return acessoService.salvar(acesso);
	}

}
