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
	
	/**
	 * Retorna uma cópia do cache de imóveis
	 */
	public static List<Imovel> getImoveis() {
		logger.info("Retornando imóveis salvos no cache");
		
		List<Imovel> copy = new ArrayList<Imovel>();
		copy.addAll(cache);
		
		return copy;
	}
	
	/**
	 * Retorna uma cópia do cache de imóveis do Zap
	 */
	public static List<Imovel> getZapImoveis() {
		logger.info("Retornando imóveis salvos no cache do Zap");
		
		List<Imovel> copy = new ArrayList<Imovel>();
		copy.addAll(zapCache);
		
		return copy;
	}

	/**
	 * Retorna uma cópia do cache de imóveis do Vivareal
	 */
	public static List<Imovel> getVivarealImoveis() {
		logger.info("Retornando imóveis salvos no cache do Zap");
		
		List<Imovel> copy = new ArrayList<Imovel>();
		copy.addAll(vivarealCache);
		
		return copy;
	}
	
	/**
	 * Retorna verdadeiro se o cache possui conteúdo e falso caso contrário
	 */
	public static boolean hasValues() {
		return !cache.isEmpty();
	}
	
	/**
	 * Retorna verdadeiro se o cache do Zap possui conteúdo e falso caso contrário
	 */
	public static boolean zapHasValues() {
		return !zapCache.isEmpty();
	}
	
	/**
	 * Retorna verdadeiro se o cache do Vivareal possui conteúdo e falso caso contrário
	 */
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

	/**
	 * Apaga o conteúdo atual do cache e atualiza de acordo com a lista passada
	 * 
	 * @param imoveis
	 */
	public static void updateImoveis(List<Imovel> imoveis) {
		logger.info("Atualizando cache...");
		
		limpaCache();
		cache.addAll(imoveis);
		
		logger.info("Cache atualizado.");
	}
	
	/**
	 * Apaga o conteúdo atual do cache do Zap e atualiza de acordo com a lista passada
	 * 
	 * @param imoveis
	 */
	public static void updateZapImoveis(List<Imovel> imoveis) {
		logger.info("Atualizando cache do Zap...");
		
		zapCache = new ArrayList<Imovel>();
		zapCache.addAll(imoveis);
		
		logger.info("Cache do Zap atualizado.");
	}
	
	/**
	 * Apaga o conteúdo atual do cache do Vivareal e atualiza de acordo com a lista passada
	 * 
	 * @param imoveis
	 */
	public static void updateVivarealImoveis(List<Imovel> imoveis) {
		logger.info("Atualizando cache do Vivareal...");
		
		vivarealCache = new ArrayList<Imovel>();
		vivarealCache.addAll(imoveis);
		
		logger.info("Cache do Vivareal atualizado.");
	}
	
	/**
	 * Retorna o total de imóveis que constam no cache do Zap
	 */
	public static int getTotalZapCount() {
		return zapCache.size();
	}
	
	/**
	 * Retorna o total de imóveis que constam no cache do Zap
	 */
	public static int getTotalVivarealCount() {
		return vivarealCache.size();
	}
}
