package br.com.jproberto.desafioGrupoZap.consumer.wrapper;

import java.util.List;

import br.com.jproberto.desafioGrupoZap.core.model.Imovel;

/**
 * Representa o resultado da chamada feita ao source
 */
public class GrupoZapWrapper {
	private List<Imovel> imoveis;

	public List<Imovel> getImoveis() {
		return imoveis;
	}

	public void setImoveis(List<Imovel> imoveis) {
		this.imoveis = imoveis;
	}
}
