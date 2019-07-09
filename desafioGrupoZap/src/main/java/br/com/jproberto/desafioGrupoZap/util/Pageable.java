package br.com.jproberto.desafioGrupoZap.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe utilitária que cuida da paginação de uma lista e todos os métodos necessários para acessar as páginas desejadas
 *  
 */
public class Pageable<T> {

	public static final int DEFAULT_PAGE_SIZE = PropertiesHandler.getInteger(PropertiesKeys.DEFAULT_PAGE_SIZE);
	
	private List<T> list;
	
	private int pageSize = DEFAULT_PAGE_SIZE;
	private int currentPage;
	private int startingIndex = 0;
	private int endingIndex = pageSize;
	private int maxPages;
	
	private Logger logger = LoggerFactory.getLogger(Pageable.class);
	
	public Pageable(List<T> list) {
		logger.info("Criando paginação para lista.");
		
		this.list = list;
		this.currentPage = 1;
		this.maxPages = 1;
		
		calculatePages();
	}
	
	/**
	 * Calcula o número de páginas para a lista passada
	 */
	private void calculatePages() {
		logger.info("Calculando total de páginas...");
		
		if (pageSize > 0) {
			int listSize = list.size();
			maxPages = listSize / pageSize;
			
			if (listSize % pageSize != 0) {
				maxPages++;
			}
		}
		
		logger.info("Total de páginas calculadas: " + maxPages);
	}
	
	/**
	 * Retorna a lista que deu origem à paginação
	 */
	public List<T> getList() {
		return list;
	}
	
	/**
	 * Retorna a lista correspondente à página definida como atual
	 */
	public List<T> getListForCurrentPage() {
		return list.subList(startingIndex, endingIndex);
	}
	
	/**
	 * Retorna o tamanho definido atualmente para cada página 
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	/**
	 * Define o tamanho da página
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * Retorna o número da página atual
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * Define a página atual
	 * @param page
	 */
	public void setCurrentPage(int page) {
		logger.info("Definindo página atual para " + page);
		
		if (page >= maxPages) {
			currentPage = maxPages;
		} else if (page < 0) {
			currentPage = 1;
		} else {
			currentPage = page;
		}
		
		startingIndex = pageSize * (currentPage - 1);
		if (startingIndex < 0) {
			startingIndex = 0;
		}
		
		endingIndex = startingIndex + pageSize;
		int listSize = list.size();
		if (endingIndex > listSize) {
			endingIndex = listSize;
		}
	}
	
	/**
	 * Retorna o número de páginas
	 */
	public int getMaxPages() {
		return maxPages;
	}
}
