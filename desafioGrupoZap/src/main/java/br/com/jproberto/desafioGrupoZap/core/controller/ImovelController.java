package br.com.jproberto.desafioGrupoZap.core.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import br.com.jproberto.desafioGrupoZap.core.model.Response;
import br.com.jproberto.desafioGrupoZap.core.service.ImovelService;
import br.com.jproberto.desafioGrupoZap.util.PropertiesHandler;
import br.com.jproberto.desafioGrupoZap.util.PropertiesKeys;
import br.com.jproberto.desafioGrupoZap.util.Validator;

public abstract class ImovelController {
	
	@GetMapping
	public ResponseEntity<Response> getImoveis(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "pageSize", required = false) String pageSize) {
		int pageInt, pageSizeInt;

		if (page == null || page.equals("")) {
			pageInt = 1;
		} else if (Validator.isNumber(page)) {
			pageInt = Integer.parseInt(page);
		} else {
			//TODO lançar exceção ou código http para parametro invalido
			throw new IllegalArgumentException();
		}
		
		List<Imovel> imoveis;
		if (pageSize != null && !pageSize.equals("") && Validator.isNumber(pageSize)) {
			pageSizeInt = Integer.parseInt(pageSize);
			
			getLogger().info("Buscando imóveis da página " + page + " com " + pageSize + " elementos.");
			
			imoveis = getService().getImoveis(pageInt, pageSizeInt);
		} else {
			imoveis  = getService().getImoveis(pageInt);
			pageSizeInt = PropertiesHandler.getInteger(PropertiesKeys.DEFAULT_PAGE_SIZE);
		}
		
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
