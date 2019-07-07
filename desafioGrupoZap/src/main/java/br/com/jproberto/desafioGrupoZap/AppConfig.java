package br.com.jproberto.desafioGrupoZap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Classe responsável por usar as configurações automáticas do Spring para lançar o projeto
 */

@SpringBootApplication
@EnableScheduling
public class AppConfig {
	public static void main(String[] args) {
		SpringApplication.run(AppConfig.class, args);
	}
}