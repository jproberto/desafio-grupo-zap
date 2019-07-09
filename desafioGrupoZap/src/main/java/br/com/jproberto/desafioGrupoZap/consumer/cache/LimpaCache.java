package br.com.jproberto.desafioGrupoZap.consumer.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *	Tarefa agendada para fazer a limpeza do cache de tempos em tempos para que os dados não fiquem obsoletos 
 */
@Component
public class LimpaCache {
	
	private Logger logger = LoggerFactory.getLogger(LimpaCache.class);

	private static final long MULTIPLICADOR_MINUTO	= 60000;
	private static final long TIMER_EM_MINUTOS		= 60;
	private static final long TIMER_EM_HORAS		= 12;
	
	private static final long rate = TIMER_EM_MINUTOS * MULTIPLICADOR_MINUTO * TIMER_EM_HORAS;

	//Chamado a cada hora para limpar o cache de modo que as novas requisições sejam atualizadas
	@Scheduled(fixedRate = rate)
	public void limpaImoveisCache() {
		logger.info("Limpeza de cache agendada para cada " + TIMER_EM_HORAS + " horas." );
		
		ImovelCache.limpaCache();
	}
}
