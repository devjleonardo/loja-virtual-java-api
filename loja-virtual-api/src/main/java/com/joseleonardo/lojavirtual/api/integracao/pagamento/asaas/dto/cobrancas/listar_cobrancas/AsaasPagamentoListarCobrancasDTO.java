package com.joseleonardo.lojavirtual.api.integracao.pagamento.asaas.dto.cobrancas.listar_cobrancas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AsaasPagamentoListarCobrancasDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// Tipo de objeto
	private String object;

	// Indica se há mais uma página a ser buscada
	private boolean hasMor;

	// Quantidade total de itens para os filtros informados
	private int totalCount;

	// Quantidade de objetos por página
	private int limit;

	// Posição do objeto a partir do qual a página deve ser carregada
	private int offset;

	private List<AsaasPagamentoListarCobrancasDataDTO> data = new ArrayList<>();

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public boolean isHasMor() {
		return hasMor;
	}

	public void setHasMor(boolean hasMor) {
		this.hasMor = hasMor;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public List<AsaasPagamentoListarCobrancasDataDTO> getData() {
		return data;
	}

	public void setData(List<AsaasPagamentoListarCobrancasDataDTO> data) {
		this.data = data;
	}

}
