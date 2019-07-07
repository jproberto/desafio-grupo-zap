package br.com.jproberto.desafioGrupoZap.consumer.cache;

import java.util.ArrayList;
import java.util.List;

import br.com.jproberto.desafioGrupoZap.core.model.Imovel;

/**
 * Representa um cache para guardar os imóveis por um período pré-determinado e evitar chamadas excessivas à API externa.
 *
 */
public class ImovelCache {
	private static List<Imovel> cache = new ArrayList<Imovel>();
	private static List<Imovel> zapCache = new ArrayList<Imovel>();
	private static List<Imovel> vivaRealCache = new ArrayList<Imovel>();
	
	public static List<Imovel> getImoveis() {
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
	
	public static boolean vivaRealHasValues() {
		return !vivaRealCache.isEmpty();
	}
	
	/**
	 * Método chamado para apagar todo o cache guardado e iniciar um novo, sem nenhuma informação.
	 */
	public static void limpaCache() {
		cache = new ArrayList<Imovel>();
		zapCache = new ArrayList<Imovel>();
		vivaRealCache = new ArrayList<Imovel>();
	}

	public static void updateImoveis(List<Imovel> imoveis) {
		cache = new ArrayList<Imovel>();
		cache.addAll(imoveis);
	}
	
	public static void updateZapImoveis(List<Imovel> imoveis) {
		zapCache = new ArrayList<Imovel>();
		zapCache.addAll(imoveis);
	}
	
	public static void updateVivaRealImoveis(List<Imovel> imoveis) {
		vivaRealCache = new ArrayList<Imovel>();
		vivaRealCache.addAll(imoveis);
	}
}
