package br.com.jproberto.desafioGrupoZap.core.model;

public class Address {

	private String city;
	private String neighborhood;
	private GeoLocation geoLocation;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	public boolean isInGrupoZapBoudingBox() {
		return geoLocation.isInGrupoZapBoudingBox();
	}
}
