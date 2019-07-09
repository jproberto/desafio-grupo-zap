package br.com.jproberto.desafioGrupoZap.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jproberto.desafioGrupoZap.consumer.cache.ImovelCache;
import br.com.jproberto.desafioGrupoZap.core.service.ImovelService;
import br.com.jproberto.desafioGrupoZap.core.service.VivarealService;
import br.com.jproberto.desafioGrupoZap.core.service.ZapService;

/**
 * Classe que recebe as requisições feitas para /zap. Herda o comportamento de {@link ImovelController} e define que seu serviço é {@link ZapService}
 */
@RestController
@RequestMapping("/zap")
public class ZapController extends ImovelController {

	@Autowired
	private ZapService service;

	@Override
	protected ImovelService getService() {
		return service;
	}

	@Override
	protected int getTotalCount() {
		return ImovelCache.getTotalZapCount();
	}

	@Override
	protected Logger getLogger() {
		return LoggerFactory.getLogger(ZapController.class);
	}
}
