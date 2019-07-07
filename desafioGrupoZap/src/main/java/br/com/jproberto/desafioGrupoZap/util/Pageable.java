package br.com.jproberto.desafioGrupoZap.util;

import java.util.List;

public class Pageable<T> {

	public static final int DEFAULT_PAGE_SIZE = PropertiesHandler.getInteger(PropertiesKeys.DEFAULT_PAGE_SIZE);
	
	private List<T> list;
	
	private int pageSize = DEFAULT_PAGE_SIZE;
	private int currentPage;
	private int startingIndex = 0;
	private int endingIndex = pageSize;
	private int maxPages;
	
	public Pageable(List<T> list) {
		this.list = list;
		this.currentPage = 1;
		this.maxPages = 1;
		
		calculatePages();
	}
	
	private void calculatePages() {
		if (pageSize > 0) {
			int listSize = list.size();
			maxPages = listSize / pageSize;
			
			if (listSize % pageSize != 0) {
				maxPages++;
			}
		}
	}
	
	public List<T> getList() {
		return list;
	}
	
	public List<T> getListForCurrentPage() {
		return list.subList(startingIndex, endingIndex);
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public void setCurrentPage(int page) {
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
	
	public int getMaxPages() {
		return maxPages;
	}
	
	public int getPreviousPage() {
		if (currentPage > 1) {
			return currentPage - 1;
		} else {
			return 0;
		}
	}
	
	public int getNextPage() {
		if (currentPage < maxPages) {
			return currentPage + 1;
		} else {
			return 0;
		}
	}
}
