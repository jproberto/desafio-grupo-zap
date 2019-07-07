package br.com.jproberto.desafioGrupoZap.util;

public class Validator {

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
