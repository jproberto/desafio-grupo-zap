package br.com.jproberto.desafioGrupoZap.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.jproberto.desafioGrupoZap.consumer.service.ConsumerService;
import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import br.com.jproberto.desafioGrupoZap.util.Pageable;

public abstract class ImovelService {
	
	@Autowired
	protected ConsumerService consumerService;
	
	private Pageable<Imovel> paginacao;

	public List<Imovel> getImoveis(int currentPage) {
		return getImoveis(currentPage, 0);
	}
	
	public List<Imovel> getImoveis(int currentPage, int pageSize) {
		if (paginacao == null) {
			List<Imovel> imoveis = getFromCache();
			paginacao = new Pageable<Imovel>(imoveis);
		}
		
		if (pageSize > 0) {
			paginacao.setPageSize(pageSize);
		}
		
		paginacao.setCurrentPage(currentPage);
		
		return paginacao.getListForCurrentPage();
	}

	protected abstract List<Imovel> getFromCache();
}
