package br.com.jproberto.desafioGrupoZap.util;

/**
 * Classe que faz validações pertinentes à aplicação 
 */
public class Validator {

	/**
	 * Verifica se a String passada é um número válido
	 * @param string
	 */
	public static boolean isNumber(String string) {
		char[] characters = string.toCharArray();

		for (char c : characters) {
		    if (!Character.isDigit(c)) {
		    	return false;
		    }
		}

		return true;
	}
}
