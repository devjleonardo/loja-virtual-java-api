package com.joseleonardo.lojavirtual.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.StatusRastreio;
import com.joseleonardo.lojavirtual.repository.StatusRastreioRepository;
import com.joseleonardo.lojavirtual.repository.VendaCompraLojaVirtualRepository;

@RestController
public class StatusRastreioController {

	@Autowired
	private StatusRastreioRepository statusRastreioRepository;
	
	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	@ResponseBody
	@GetMapping(value = "**/buscarStatusRastreioPorVendaId/{vendaId}")
	public ResponseEntity<List<StatusRastreio>> buscarStatusRastreioPorVendaId(
			@PathVariable("vendaId") Long vendaId) throws LojaVirtualException {
		if (!vendaCompraLojaVirtualRepository.findById(vendaId).isPresent()) {
			throw new LojaVirtualException("Não econtrou nenhuma venda com o código: " 
		            + vendaId);
		}
		
		List<StatusRastreio> statusRastreios = statusRastreioRepository
				.buscarStatusRastreioPorVendaId(vendaId);

		return new ResponseEntity<List<StatusRastreio>>(statusRastreios, HttpStatus.OK);
	}
	
}
