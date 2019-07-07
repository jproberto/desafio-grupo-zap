package br.com.jproberto.desafioGrupoZap.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste")
public final class TesteController {

	@GetMapping
	public String index() {
		return "Hello, world!";
	}
}
