package br.com.jproberto.desafioGrupoZap;

import br.com.jproberto.desafioGrupoZap.util.PropertiesHandler;
import br.com.jproberto.desafioGrupoZap.util.PropertiesKeys;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PropertiesTest extends TestCase {
	public static Test suite() {
		return new TestSuite(PropertiesTest.class);
	}
	
	public void testGetProperty() {
		//Essa só vale para enquanto estiver em DEV
		final String value = "http://grupozap-code-challenge.s3-website-us-east-1.amazonaws.com/sources/source-sample.json";
		assertTrue(value.equals(PropertiesHandler.getString(PropertiesKeys.API_SOURCE_IMOVEIS)));
	}
}
