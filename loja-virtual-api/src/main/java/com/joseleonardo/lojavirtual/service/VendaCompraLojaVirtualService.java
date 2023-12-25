package com.joseleonardo.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendaCompraLojaVirtualService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void deletarVendaCompraLojaVirtualPorId(Long id) {
		String sql = 
		           "BEGIN; "
		   		 + "UPDATE nota_fiscal_venda SET venda_compra_loja_virtual_id = null "
		   		 + "WHERE venda_compra_loja_virtual_id = " + id + "; "
		   		 + "DELETE FROM item_venda_loja WHERE venda_compra_loja_virtual_id = " + id + "; "
		   		 + "DELETE FROM status_rastreio WHERE venda_compra_loja_virtual_id = " + id + "; "
		   		 + "DELETE FROM venda_compra_loja_virtual WHERE id = " + id + "; " 
		   		 + "DELETE FROM nota_fiscal_venda WHERE venda_compra_loja_virtual_id IS null; "
		   		 + "COMMIT; ";
		
		jdbcTemplate.execute(sql);
	} 
	
}
