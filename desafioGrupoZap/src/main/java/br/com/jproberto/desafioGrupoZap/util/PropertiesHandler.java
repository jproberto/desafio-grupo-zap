package br.com.jproberto.desafioGrupoZap.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesHandler {

	private static Properties props;
    private static FileInputStream file;
	
    //TODO usar logger
	static {
		props = new Properties();
		try {
			file = new FileInputStream(PropertiesKeys.PROPERTY_FILE);
			props.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getString(String propertyKey) {
		return props.getProperty(propertyKey);
	}
	
	public static int getInteger(String propertyKey) {
		return Integer.parseInt(getString(propertyKey));
	}
	
	public static double getDouble(String propertyKey) {
		return Double.parseDouble(getString(propertyKey));
	}
}
