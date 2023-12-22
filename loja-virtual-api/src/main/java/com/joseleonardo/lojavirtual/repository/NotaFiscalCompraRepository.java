package com.joseleonardo.lojavirtual.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.joseleonardo.lojavirtual.model.NotaFiscalCompra;

@Repository
@Transactional
public interface NotaFiscalCompraRepository extends JpaRepository<NotaFiscalCompra, Long> {

	@Query("SELECT nfc FROM NotaFiscalCompra nfc "
		 + "WHERE UPPER(TRIM(nfc.descricaoObservacao)) LIKE %?1%")
	List<NotaFiscalCompra> buscarNotaFiscalCompraPorDescricaoObservacao(String descricaoObservacao);

	@Query("SELECT nfc FROM NotaFiscalCompra nfc WHERE nfc.pessoa.id = ?1")
	List<NotaFiscalCompra> buscarNotaFiscalCompraPorPessoaId(Long pessoaId);

	@Query("SELECT nfc FROM NotaFiscalCompra nfc WHERE nfc.contaPagar.id = ?1")
	List<NotaFiscalCompra> buscarNotaFiscalCompraPorContaPagarId(Long contaPagarId);

	@Query("SELECT nfc FROM NotaFiscalCompra nfc WHERE nfc.empresa.id = ?1")
	List<NotaFiscalCompra> buscarNotaFiscalCompraPorEmpresaId(Long empresaId);

}
