package com.joseleonardo.lojavirtual.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.joseleonardo.lojavirtual.model.dto.RelatorioNotaFiscalCompraProdutosCompradosDTO;

@Service
public class NotaFiscalCompraService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<RelatorioNotaFiscalCompraProdutosCompradosDTO> gerarRelatorioProdutosComprados(
			RelatorioNotaFiscalCompraProdutosCompradosDTO relatorioNotaFiscalCompraProdutosCompradosDTO) {
		List<RelatorioNotaFiscalCompraProdutosCompradosDTO> retorno = new ArrayList<>();
		
		String sql= 
				  "SELECT p.id AS codigoProduto, "
				+ "       p.nome AS nomeProduto, "
				+ "       p.valor_venda AS valorVendaProduto, "
				+ "       nip.quantidade AS quantidadeComprada, "
				+ "       pj.id AS codigoFornecedor, "
				+ "       pj.nome AS nomeFornecedor, "
				+ "       nfc.data_compra AS dataCompra "
				+ "FROM nota_fiscal_compra AS nfc "
				+ "INNER JOIN nota_item_produto AS nip ON nfc.id = nip.nota_fiscal_compra_id "
				+ "INNER JOIN produto AS p ON nip.produto_id = p.id "
				+ "INNER JOIN pessoa_juridica AS pj ON nfc.pessoa_id = pj.id "
				+ "WHERE ";		
		
		sql += "nfc.data_compra >= '" + relatorioNotaFiscalCompraProdutosCompradosDTO.getDataInicial() + "' "
			 + "AND nfc.data_compra <= '" + relatorioNotaFiscalCompraProdutosCompradosDTO.getDataFinal() + "' ";
		
		String codigoNotaFiscalCompra = relatorioNotaFiscalCompraProdutosCompradosDTO.getCodigoNotaFiscalCompra();
		
		if (codigoNotaFiscalCompra != null && !codigoNotaFiscalCompra.isEmpty() && !codigoNotaFiscalCompra.isBlank()) {
			sql += "AND nfc.id = " + codigoNotaFiscalCompra + " ";
		}
		
		String codigoProduto = relatorioNotaFiscalCompraProdutosCompradosDTO.getCodigoProduto();
		
		if (codigoProduto != null && !codigoProduto.isEmpty() && !codigoProduto.isBlank()) {
			sql += "AND p.id = " + codigoProduto + " ";
		}
		
		String nomeProduto = relatorioNotaFiscalCompraProdutosCompradosDTO.getNomeProduto();
		
		if (nomeProduto != null && !nomeProduto.isEmpty() && !nomeProduto.isBlank()) {
			sql += "AND UPPER(p.nome) LIKE UPPER('%" + nomeProduto + "%')";
		}
		
		String nomeFornecedor = relatorioNotaFiscalCompraProdutosCompradosDTO.getNomeFornecedor();
		
		if (nomeFornecedor != null  && !nomeFornecedor.isEmpty() && !nomeFornecedor.isBlank()) {
			sql += "AND UPPER(pj.nome) LIKE UPPER('%" + nomeFornecedor + "%')";
		}
		
		retorno = jdbcTemplate.query(sql, 
				new BeanPropertyRowMapper<>(RelatorioNotaFiscalCompraProdutosCompradosDTO.class));
		
		return retorno;
	}

}
