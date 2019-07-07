package br.com.jproberto.desafioGrupoZap.core.model;

public class GeoLocation {
	private String precision;
	private Location location;

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isInGrupoZapBoudingBox() {
		return location.isInGrupoZapBoudingBox();
	}

}
