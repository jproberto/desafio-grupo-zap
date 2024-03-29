package br.com.jproberto.desafioGrupoZap.core.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import br.com.jproberto.desafioGrupoZap.core.model.Response;
import br.com.jproberto.desafioGrupoZap.core.service.ImovelService;
import br.com.jproberto.desafioGrupoZap.exception.JsonToImoveisException;
import br.com.jproberto.desafioGrupoZap.util.PropertiesHandler;
import br.com.jproberto.desafioGrupoZap.util.PropertiesKeys;
import br.com.jproberto.desafioGrupoZap.util.Validator;

/**
 * Classe abstrata que define a chamada da API e reúne a lógica para manipular as buscas, mas deixa pontos de inserção para que as classes que a estendem possam adicionar suas lógicas específicas
 *
 */
public abstract class ImovelController {
	
	@GetMapping
	public ResponseEntity<Response> getImoveis(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "pageSize", required = false) String pageSize) throws JsonToImoveisException {
		int pageInt, pageSizeInt;

		//Se a página não foi especificada, considera-se a primeira
		if (page == null || page.equals("")) {
			pageInt = 1;
		} else if (Validator.isNumber(page)) {
			//Se foi especificada, verifica se é um número válido para prosseguir
			pageInt = Integer.parseInt(page);
		} else {
			//Se não for um número válido, lança uma exceção
			throw new IllegalArgumentException();
		}
		
		List<Imovel> imoveis;
		//Verifica se o tamanho da página foi especificado e é válido para buscar os imóveis
		if (pageSize != null && !pageSize.equals("") && Validator.isNumber(pageSize)) {
			pageSizeInt = Integer.parseInt(pageSize);
			
			getLogger().info("Buscando imóveis da página " + page + " com " + pageSize + " elementos.");
			
			imoveis = getService().getImoveis(pageInt, pageSizeInt);
		} else {
			//Se não for, usa o tamanho de página padrão
			imoveis  = getService().getImoveis(pageInt);
			pageSizeInt = PropertiesHandler.getInteger(PropertiesKeys.DEFAULT_PAGE_SIZE);
		}

		//Monta o objeto response para ser retornado
		Response response = new Response();
		response.setPageNumber(pageInt);
		response.setPageSize(pageSizeInt);
		response.setTotalCount(getTotalCount());
		response.setListings(imoveis);
		
		getLogger().info("Retornando imóveis.");
		
		return ResponseEntity.ok(response);
	}
	
	protected abstract ImovelService getService();
	
	protected abstract int getTotalCount();
	
	protected abstract Logger getLogger();
}
