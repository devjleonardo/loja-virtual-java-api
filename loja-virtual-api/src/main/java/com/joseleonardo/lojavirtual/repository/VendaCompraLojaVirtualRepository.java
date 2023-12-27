package com.joseleonardo.lojavirtual.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.joseleonardo.lojavirtual.model.VendaCompraLojaVirtual;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository 
        extends JpaRepository<VendaCompraLojaVirtual, Long> {

	@Query("SELECT vclv FROM VendaCompraLojaVirtual vclv "
		 + "WHERE vclv.id = ?1 AND vclv.excluido = false")
	Optional<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualPorIdSemExclusao(Long id);
	
	@Query("SELECT iv.vendaCompraLojaVirtual FROM ItemVendaLoja iv "
		 + "WHERE iv.vendaCompraLojaVirtual.excluido = false AND iv.produto.id = ?1")
	List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualPorProdutoId(Long produtoId);
	
	@Query("SELECT DISTINCT(iv.vendaCompraLojaVirtual) FROM ItemVendaLoja iv "
	     + "WHERE iv.vendaCompraLojaVirtual.excluido = false "
	     + "AND UPPER(TRIM(iv.produto.nome)) LIKE %?1%")
	List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualPorNomeDoProduto(String nomeDoProduto);

	@Query("SELECT DISTINCT(iv.vendaCompraLojaVirtual) FROM ItemVendaLoja iv "
		     + "WHERE iv.vendaCompraLojaVirtual.excluido = false "
		     + "AND UPPER(TRIM(iv.vendaCompraLojaVirtual.pessoa.nome)) LIKE %?1%")
	List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualPorNomeDoCliente(String nomeDoCliente);
	
	@Query("SELECT DISTINCT(iv.vendaCompraLojaVirtual) FROM ItemVendaLoja iv "
		     + "WHERE iv.vendaCompraLojaVirtual.excluido = false "
		     + "AND UPPER(TRIM(iv.vendaCompraLojaVirtual.enderecoEntrega.logradouro)) LIKE %?1%")
	List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualPorEnderecoDeEntrega(
			String enderecoDeEntrega);

	@Query("SELECT DISTINCT(iv.vendaCompraLojaVirtual) FROM ItemVendaLoja iv "
		     + "WHERE iv.vendaCompraLojaVirtual.excluido = false "
		     + "AND UPPER(TRIM(iv.vendaCompraLojaVirtual.enderecoCobranca.logradouro)) LIKE %?1%")
	List<VendaCompraLojaVirtual> buscarVendaCompraLojaVirtualPorEnderecoDeCobranca(
			String enderecoDeCobranca);
	
}
