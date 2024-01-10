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
import com.joseleonardo.lojavirtual.model.NotaFiscalVenda;
import com.joseleonardo.lojavirtual.repository.NotaFiscalVendaRepository;
import com.joseleonardo.lojavirtual.repository.VendaCompraLojaVirtualRepository;

@RestController
public class NotaFiscalVendaController {

	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;
	
	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	@ResponseBody
	@GetMapping(value = "**/buscarNotaFiscalVendaPorVendaId/{vendaId}")
	public ResponseEntity<List<NotaFiscalVenda>> buscarNotaFiscalVendaPorVendaId(
			@PathVariable("vendaId") Long vendaId) throws LojaVirtualException {
		if (!vendaCompraLojaVirtualRepository.findById(vendaId).isPresent()) {
			throw new LojaVirtualException("Não econtrou nenhuma venda com o código: " 
		            + vendaId);
		}
		
		List<NotaFiscalVenda> notasFiscalVenda = notaFiscalVendaRepository
				.buscarNotaFiscalVendaPorVendaId(vendaId);

		return new ResponseEntity<List<NotaFiscalVenda>>(notasFiscalVenda, HttpStatus.OK);
	}
	
}
