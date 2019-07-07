package br.com.jproberto.desafioGrupoZap.core.model;

import java.util.List;

public class Imovel {
	private int usableAreas;
	private String listingType;
	private String createdAt;
	private String listingStatus;
	private String id;
	private int parkingSpaces;
	private String updatedAt;
	private boolean owner;
	private List<String> images;
	private Address address;
	private int bathrooms;
	private int bedrooms;
	private PricingInfos pricingInfos;

	public int getUsableAreas() {
		return usableAreas;
	}

	public void setUsableAreas(int usableAreas) {
		this.usableAreas = usableAreas;
	}

	public String getListingType() {
		return listingType;
	}

	public void setListingType(String listingType) {
		this.listingType = listingType;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getListingStatus() {
		return listingStatus;
	}

	public void setListingStatus(String listingStatus) {
		this.listingStatus = listingStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getParkingSpaces() {
		return parkingSpaces;
	}

	public void setParkingSpaces(int parkingSpaces) {
		this.parkingSpaces = parkingSpaces;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public boolean isOwner() {
		return owner;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public int getBathrooms() {
		return bathrooms;
	}

	public void setBathrooms(int bathrooms) {
		this.bathrooms = bathrooms;
	}
	
	public int getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}

	public PricingInfos getPricingInfos() {
		return pricingInfos;
	}

	public void setPricingInfos(PricingInfos pricingInfos) {
		this.pricingInfos = pricingInfos;
	}

	public boolean isInGrupoZapBoudingBox() {
		return address.isInGrupoZapBoudingBox();
	}
}
