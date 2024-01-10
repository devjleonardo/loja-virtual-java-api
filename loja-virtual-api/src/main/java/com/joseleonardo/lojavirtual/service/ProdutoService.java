package com.joseleonardo.lojavirtual.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.joseleonardo.lojavirtual.api.dto.relatorio.RelatorioAlertaEstoqueBaixoDeProdutoDTO;

@Service
public class ProdutoService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Título: Relatório de estoque baixo (Reposição).
	 * 
	 * Este relatório retorna os produtos que estão com o estoque menor ou igual a quantidade 
	 * definda no campo quantidade_alerta_estoque do produto.
	 * 
	 * @param relatorioAlertaEstoqueBaixoDeProdutoDTO RelatorioAlertaEstoqueBaixoDeProdutoDTO
	 * @param dataInicial e dataFinal são parâmetros obrigatórios
	 * @return List<RelatorioAlertaEstoqueBaixoDeProdutoDTO>
	 */
	public List<RelatorioAlertaEstoqueBaixoDeProdutoDTO> gerarRelatorioAlertaEstoqueBaixoDeProduto(
			RelatorioAlertaEstoqueBaixoDeProdutoDTO relatorioAlertaEstoqueBaixoDeProdutoDTO) {
		List<RelatorioAlertaEstoqueBaixoDeProdutoDTO> retorno = new ArrayList<>();
		
		String sql= 
				  "SELECT p.id AS codigoProduto, "
				+ "       p.nome AS nomeProduto, "
				+ "       p.valor_venda AS valorVendaProduto, "
				+ "       nip.quantidade AS quantidadeComprada, "
				+ "       pj.id AS codigoFornecedor, "
				+ "       pj.nome AS nomeFornecedor, "
				+ "       nfc.data_compra AS dataCompra, "
				+ "       p.quantidade_estoque AS quantidadeEstoque, "
				+ "       p.quantidade_alerta_estoque AS quantidadeAlertaEstoque "
				+ "FROM produto AS p "
				+ "INNER JOIN nota_item_produto AS nip ON p.id = nip.produto_id "
				+ "INNER JOIN nota_fiscal_compra AS nfc ON nip.nota_fiscal_compra_id = nfc.id "
				+ "INNER JOIN pessoa_juridica AS pj ON nfc.pessoa_id = pj.id "
				+ "WHERE ";
		
		sql += "nfc.data_compra >= '" + relatorioAlertaEstoqueBaixoDeProdutoDTO.getDataInicial() + "' AND ";
		sql += "nfc.data_compra <= '" + relatorioAlertaEstoqueBaixoDeProdutoDTO.getDataFinal() + "' ";
		sql += "AND alerta_quantidade_estoque = true AND p.quantidade_estoque <= quantidade_alerta_estoque ";
		
		String codigoNotaFiscalCompra = relatorioAlertaEstoqueBaixoDeProdutoDTO.getCodigoNotaFiscalCompra();
		
		if (codigoNotaFiscalCompra != null && !codigoNotaFiscalCompra.isEmpty() && !codigoNotaFiscalCompra.isBlank()) {
			sql += "AND nfc.id = " + codigoNotaFiscalCompra + " ";
		}
		
		String codigoProduto = relatorioAlertaEstoqueBaixoDeProdutoDTO.getCodigoProduto();
		
		if (codigoProduto != null && !codigoProduto.isEmpty() && !codigoProduto.isBlank()) {
			sql += "AND p.id = " + codigoProduto + " ";
		}
		
		String nomeProduto = relatorioAlertaEstoqueBaixoDeProdutoDTO.getNomeProduto();
		
		if (nomeProduto != null && !nomeProduto.isEmpty() && !nomeProduto.isBlank()) {
			sql += "AND UPPER(p.nome) LIKE UPPER('%" + nomeProduto + "%')";
		}
		
		String nomeFornecedor = relatorioAlertaEstoqueBaixoDeProdutoDTO.getNomeFornecedor();
		
		if (nomeFornecedor != null  && !nomeFornecedor.isEmpty() && !nomeFornecedor.isBlank()) {
			sql += "AND UPPER(pj.nome) LIKE UPPER('%" + nomeFornecedor + "%')";
		}
		
		retorno = jdbcTemplate.query(sql, 
				new BeanPropertyRowMapper<>(RelatorioAlertaEstoqueBaixoDeProdutoDTO.class));
		
		return retorno;
	}
	
}
