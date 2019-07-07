package br.com.jproberto.desafioGrupoZap.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import br.com.jproberto.desafioGrupoZap.core.service.ZapService;
import br.com.jproberto.desafioGrupoZap.util.Validator;

@RestController
@RequestMapping("/zap")
public class ZapController {

	@Autowired
	private ZapService service;
	
	@GetMapping
	public ResponseEntity<List<Imovel>> getImovel(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "pageSize", required = false) String pageSize) {
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
			imoveis = service.getImoveis(pageInt, pageSizeInt);
		} else {
			imoveis  = service.getImoveis(pageInt);
		}
		
		//TODO o retorno não é uma lista de imoveis, tem que montar o início com as informações de paginação
		return ResponseEntity.ok(imoveis);
	}
}
