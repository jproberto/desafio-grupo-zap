package br.com.jproberto.desafioGrupoZap.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.jproberto.desafioGrupoZap.consumer.service.ConsumerService;
import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import br.com.jproberto.desafioGrupoZap.exception.JsonToImoveisException;
import br.com.jproberto.desafioGrupoZap.util.Pageable;

/**
 *	Classe abstrata que recebe as chamadas dos controllers e reúne as regras de buscas com paginação, mas deixa pontos de inserção para que as classes que a estendem possam adicionar suas lógicas específicas 
 */
public abstract class ImovelService {
	
	@Autowired
	protected ConsumerService consumerService;
	
	private Pageable<Imovel> paginacao;

	public List<Imovel> getImoveis(int currentPage) throws JsonToImoveisException {
		return getImoveis(currentPage, 0);
	}
	
	public List<Imovel> getImoveis(int currentPage, int pageSize) throws JsonToImoveisException {
		//Caso ainda não haja uma lista paginada, carrega do cache 
		if (paginacao == null) {
			List<Imovel> imoveis = getFromCache();
			paginacao = new Pageable<Imovel>(imoveis);
		}
		
		//Define o tamanho da página, se esse parâmetro for válido
		if (pageSize > 0) {
			paginacao.setPageSize(pageSize);
		}
		
		//Define a página desejada
		paginacao.setCurrentPage(currentPage);
		
		//Retorna o resultado de acordo com os parâmetros
		return paginacao.getListForCurrentPage();
	}

	protected abstract List<Imovel> getFromCache() throws JsonToImoveisException;
}
