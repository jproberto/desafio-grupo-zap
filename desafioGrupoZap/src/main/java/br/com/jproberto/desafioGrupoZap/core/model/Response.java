package br.com.jproberto.desafioGrupoZap.core.model;

import java.util.List;

public class Response {

	private int pageNumber;
	private int pageSize;
	private int totalCount;
	private List<Imovel> listings;
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public List<Imovel> getListings() {
		return listings;
	}
	
	public void setListings(List<Imovel> listings) {
		this.listings = listings;
	}
}
