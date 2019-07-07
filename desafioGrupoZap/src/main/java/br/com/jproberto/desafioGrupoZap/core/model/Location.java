package br.com.jproberto.desafioGrupoZap.core.model;

import br.com.jproberto.desafioGrupoZap.util.PropertiesHandler;
import br.com.jproberto.desafioGrupoZap.util.PropertiesKeys;

public class Location {
	private double lon;
	private double lat;

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public boolean isInGrupoZapBoudingBox() {
		return lon >= PropertiesHandler.getDouble(PropertiesKeys.BOUDING_BOX_MIN_LON) 
			&& lon <= PropertiesHandler.getDouble(PropertiesKeys.BOUDING_BOX_MAX_LON) 
			&& lat >= PropertiesHandler.getDouble(PropertiesKeys.BOUDING_BOX_MIN_LAT) 
			&& lat <= PropertiesHandler.getDouble(PropertiesKeys.BOUDING_BOX_MAX_LAT);
	}

}
