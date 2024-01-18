package com.joseleonardo.lojavirtual.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PagamentoController {

	@GetMapping(value = "**/pagamento/{idVenda}")
	public ModelAndView pagamento(@PathVariable(value = "idVenda", required = false) String idVenda) {
		return new ModelAndView("pagamento");
	}
	
}
