package com.joseleonardo.lojavirtual.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.joseleonardo.lojavirtual.api.dto.venda.VendaCompraLojaVirtualDTO;
import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.service.VendaCompraLojaVirtualService;

@RestController
public class PagamentoController {
	
	@Autowired
	private VendaCompraLojaVirtualService vendaCompraLojaVirtualService;

	@GetMapping(value = "**/pagamento/{idVenda}")
	public ModelAndView pagamento(@PathVariable(value = "idVenda", required = false) Long idVenda)  {
		ModelAndView modelAndView = new ModelAndView("pagamento");
		
		VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO;
		
		try {
			vendaCompraLojaVirtualDTO = vendaCompraLojaVirtualService.buscarVendaCompraPorId(idVenda);
			
			modelAndView.addObject("venda", vendaCompraLojaVirtualDTO);
		} catch (LojaVirtualException e) {
			modelAndView.addObject("venda", new VendaCompraLojaVirtualDTO());
		}
		
		return modelAndView;
	}
	
}
