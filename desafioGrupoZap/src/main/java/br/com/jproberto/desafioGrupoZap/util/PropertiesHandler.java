package br.com.jproberto.desafioGrupoZap.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Classe que acessa o arquivo de propriedades e retorna os valores de acordo com as chaves passadas
 */
public class PropertiesHandler {

	private static Properties properties;
    private static FileInputStream file;
	
	static {
		properties = new Properties();
		try {
			file = new FileInputStream(PropertiesKeys.PROPERTY_FILE);
			properties.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retorna o valor da propriedade como {@link String}
	 * @param propertyKey
	 */
	public static String getString(String propertyKey) {
		return properties.getProperty(propertyKey);
	}

	/**
	 * Retorna o valor da propriedade como {@link Integer}
	 * @param propertyKey
	 */
	public static int getInteger(String propertyKey) {
		return Integer.parseInt(getString(propertyKey));
	}
	
	/**
	 * Retorna o valor da propriedade como {@link Double}
	 * @param propertyKey
	 */
	public static double getDouble(String propertyKey) {
		return Double.parseDouble(getString(propertyKey));
	}
}
