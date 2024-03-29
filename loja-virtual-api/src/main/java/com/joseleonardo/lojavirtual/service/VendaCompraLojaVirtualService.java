package com.joseleonardo.lojavirtual.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.joseleonardo.lojavirtual.api.dto.relatorio.RelatorioVendaCompraLojaVirtualPorStatusDTO;
import com.joseleonardo.lojavirtual.api.dto.venda.ItemVendaLojaDTO;
import com.joseleonardo.lojavirtual.api.dto.venda.VendaCompraLojaVirtualDTO;
import com.joseleonardo.lojavirtual.exception.LojaVirtualException;
import com.joseleonardo.lojavirtual.model.ItemVendaLoja;
import com.joseleonardo.lojavirtual.model.VendaCompraLojaVirtual;
import com.joseleonardo.lojavirtual.repository.VendaCompraLojaVirtualRepository;

@Service
public class VendaCompraLojaVirtualService {
	
	@Autowired
	private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public VendaCompraLojaVirtualDTO buscarVendaCompraPorId(Long id) throws LojaVirtualException {
		VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository
		        .buscarVendaCompraLojaVirtualPorIdSemExclusao(id).orElse(null);
		
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
		
		return vendaCompraLojaVirtualDTO;
	}

	/* SQL puro */
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
	
	/* SQL puro */
	public void desativarVendaCompraLojaVirtualPorIdAtravesDaExclusaoLogica(Long id) {
		String sql =
				   "BEGIN; "
				 + "UPDATE venda_compra_loja_virtual SET excluido = true WHERE id = " + id + "; "
				 + "COMMIT;";
		
		jdbcTemplate.execute(sql);
	}
	
	/* SQL puro */
	public void ativarVendaCompraLojaVirtualPorIdDesativadaAtravesDaExclusaoLogica(Long id) {
		String sql =
				   "BEGIN; "
				 + "UPDATE venda_compra_loja_virtual SET excluido = false WHERE id = " + id + "; "
				 + "COMMIT;";
		
		jdbcTemplate.execute(sql);
	}
	
	public List<RelatorioVendaCompraLojaVirtualPorStatusDTO> gerarRelatorioVendaCompraLojaVirtualPorStatus(
			RelatorioVendaCompraLojaVirtualPorStatusDTO relatorioVendaCompraLojaVirtualPorStatusDTO) {
		List<RelatorioVendaCompraLojaVirtualPorStatusDTO> retorno = new ArrayList<>();
		
		String sql = 
				  "SELECT p.id AS codigoProduto, "
				+ "       p.nome AS nomeProduto, "
				+ "       pf.email AS emailCliente, "
				+ "       pf.telefone AS telefoneCliente, "
				+ "       p.valor_venda AS valorVendaProduto, "
				+ "       pf.id AS codigoCliente, "
				+ "       pf.nome AS nomeCliente, "
				+ "       p.quantidade_estoque AS quantidadeEstoque, "
				+ "       vclv.id AS codigoVenda, "
				+ "       vclv.status_venda_compra_loja_virtual AS statusVendaCompraLojaVirtual "
				+ "FROM venda_compra_loja_virtual AS vclv "
				+ "INNER JOIN "
				+ "   item_venda_loja AS ivl "
				+ "   ON vclv.id = ivl.venda_compra_loja_virtual_id "
				+ "INNER JOIN produto AS p ON ivl.produto_id = p.id "
				+ "INNER JOIN pessoa_fisica AS pf ON vclv.pessoa_id = pf.id "
				+ "WHERE ";
		
		sql += "vclv.data_venda >= '" + relatorioVendaCompraLojaVirtualPorStatusDTO.getDataVendaInicial() + "' AND ";
		sql += "vclv.data_venda <= '" + relatorioVendaCompraLojaVirtualPorStatusDTO.getDataVendaFinal() + "' ";
		
		String statusVendaCompraLojaVirtual = relatorioVendaCompraLojaVirtualPorStatusDTO.getStatusVendaCompraLojaVirtual();
		
		if (statusVendaCompraLojaVirtual != null && !statusVendaCompraLojaVirtual.isEmpty() && !statusVendaCompraLojaVirtual.isBlank()) {
			sql += "AND vclv.status_venda_compra_loja_virtual IN ('" + statusVendaCompraLojaVirtual + "') ";
		}
		
		String nomeProduto = relatorioVendaCompraLojaVirtualPorStatusDTO.getNomeProduto();
		
		if (nomeProduto != null && !nomeProduto.isEmpty() && !nomeProduto.isBlank()) {
			sql += "AND UPPER(p.nome) LIKE UPPER('%" + nomeProduto + "%')";
		}
		
		String nomeCliente = relatorioVendaCompraLojaVirtualPorStatusDTO.getNomeCliente();
		
		if (nomeCliente != null && !nomeCliente.isEmpty() && !nomeCliente.isBlank()) {
			sql += "AND UPPER(pf.nome) LIKE UPPER('%" + nomeCliente + "%')";
		}
		
		retorno = jdbcTemplate.query(sql, 
				new BeanPropertyRowMapper<>(RelatorioVendaCompraLojaVirtualPorStatusDTO.class));
		
		return retorno;
	}
	
}
