package com.joseleonardo.lojavirtual.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.Endereco;
import com.joseleonardo.lojavirtual.model.ItemVendaLoja;
import com.joseleonardo.lojavirtual.model.PessoaFisica;
import com.joseleonardo.lojavirtual.model.VendaCompraLojaVirtual;
import com.joseleonardo.lojavirtual.model.dto.ItemVendaLojaDTO;
import com.joseleonardo.lojavirtual.model.dto.VendaCompraLojaVirtualDTO;
import com.joseleonardo.lojavirtual.repository.EnderecoRepository;
import com.joseleonardo.lojavirtual.repository.NotaFiscalVendaRepository;
import com.joseleonardo.lojavirtual.repository.VendaCompraLojaVirtualRepository;

@RestController
public class VendaCompraLojaVirtualController {

	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaFisicaController pessoaFisicaController;
	
	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;
	
	@ResponseBody
	@PostMapping(value = "**/salvarVendaCompraLojaVirtual")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaCompraLojaVirtual(
	        @RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) 
					throws LojaVirtualException { 
		vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		PessoaFisica pessoaFisica = pessoaFisicaController.salvarPessoaFisica(vendaCompraLojaVirtual.getPessoa()).getBody();
		vendaCompraLojaVirtual.setPessoa(pessoaFisica);
		
		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);
		
		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);
		
		vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		
		for (int i = 0; i <vendaCompraLojaVirtual.getItensVendaLoja().size(); i++) {
			vendaCompraLojaVirtual.getItensVendaLoja().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			vendaCompraLojaVirtual.getItensVendaLoja().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		}
		
		/* Salva primeiro a venda e todos os dados */
		vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.saveAndFlush(vendaCompraLojaVirtual);
		
		/* Associa a venda gravada no banco com a nota fiscal */
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		
		/* Salva a nota fiscal novamente pra ficar amarrada na venda */
		notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());
		
		VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
		vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
		vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
		vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		
		for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItensVendaLoja()) {
			ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
			itemVendaLojaDTO.setQuantidade(itemVendaLoja.getQuantidade());
			itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
			
			vendaCompraLojaVirtualDTO.getItensVendaLojaDTO().add(itemVendaLojaDTO);
		}
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(vendaCompraLojaVirtualDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultarVendaCompraLojaVirtualPorId/{id}")
	public ResponseEntity<VendaCompraLojaVirtualDTO>consultarVendaCompraLojaVirtualPorId(
			@PathVariable("id") Long id) throws LojaVirtualException {
		VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
		        .findById(id).orElse(null);
		
		if (vendaCompraLojaVirtual == null) {
			throw new LojaVirtualException("Não econtrou nenhuma venda com o código: " + id);
		}

		VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		vendaCompraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());
		vendaCompraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		vendaCompraLojaVirtualDTO.setValorDesconto(vendaCompraLojaVirtual.getValorDesconto());
		vendaCompraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
		vendaCompraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());
		vendaCompraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		
		for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItensVendaLoja()) {
			ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
			itemVendaLojaDTO.setQuantidade(itemVendaLoja.getQuantidade());
			itemVendaLojaDTO.setProduto(itemVendaLoja.getProduto());
			
			vendaCompraLojaVirtualDTO.getItensVendaLojaDTO().add(itemVendaLojaDTO);
		}
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(vendaCompraLojaVirtualDTO, HttpStatus.OK);
	}
	
}
