package br.com.jproberto.desafioGrupoZap.consumer.cache;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.jproberto.desafioGrupoZap.core.model.Imovel;

/**
 * Representa um cache para guardar os imóveis por um período pré-determinado e evitar chamadas excessivas à API externa.
 *
 */
public class ImovelCache {
	private static Logger logger = LoggerFactory.getLogger(ImovelCache.class);

	
	private static List<Imovel> cache = new ArrayList<Imovel>();
	private static List<Imovel> zapCache = new ArrayList<Imovel>();
	private static List<Imovel> vivarealCache = new ArrayList<Imovel>();
	
	public static List<Imovel> getImoveis() {
		logger.info("Retornando imóveis salvos no cache");
		
		List<Imovel> copy = new ArrayList<Imovel>();
		copy.addAll(cache);
		
		return copy;
	}
	
	public static boolean hasValues() {
		return !cache.isEmpty();
	}
	
	public static boolean zapHasValues() {
		return !zapCache.isEmpty();
	}
	
	public static boolean vivarealHasValues() {
		return !vivarealCache.isEmpty();
	}
	
	/**
	 * Método chamado para apagar todo o cache guardado e iniciar um novo, sem nenhuma informação.
	 */
	public static void limpaCache() {
		logger.info("Limpando cache...");
		
		cache = new ArrayList<Imovel>();
		zapCache = new ArrayList<Imovel>();
		vivarealCache = new ArrayList<Imovel>();
		
		logger.info("Cache limpo.");
	}

	public static void updateImoveis(List<Imovel> imoveis) {
		logger.info("Atualizando cache...");
		
		limpaCache();
		cache.addAll(imoveis);
		
		logger.info("Cache atualizado.");
	}
	
	public static void updateZapImoveis(List<Imovel> imoveis) {
		logger.info("Atualizando cache do Zap...");
		
		zapCache = new ArrayList<Imovel>();
		zapCache.addAll(imoveis);
		
		logger.info("Cache do Zap atualizado.");
	}
	
	public static void updateVivarealImoveis(List<Imovel> imoveis) {
		logger.info("Atualizando cache do Vivareal...");
		
		vivarealCache = new ArrayList<Imovel>();
		vivarealCache.addAll(imoveis);
		
		logger.info("Cache do Vivareal atualizado.");
	}
	
	public static int getTotalZapCount() {
		return zapCache.size();
	}
	
	public static int getTotalVivarealCount() {
		return vivarealCache.size();
	}
}
