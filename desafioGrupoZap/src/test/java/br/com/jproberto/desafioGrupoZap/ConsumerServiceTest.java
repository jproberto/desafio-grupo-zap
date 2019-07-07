package br.com.jproberto.desafioGrupoZap;

import java.util.List;

import br.com.jproberto.desafioGrupoZap.consumer.service.ConsumerService;
import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConsumerServiceTest extends TestCase {
	public static Test suite() {
		return new TestSuite(ConsumerServiceTest.class);
	}

	public void testGetImoveis()  {
		ConsumerService service = new ConsumerService();
		List<Imovel> imoveis;
		try {
			imoveis = service.getImoveis();
		} catch (Exception e) {
			imoveis = null;
			e.printStackTrace();
		}
		assertEquals("2016-11-16T04:14:02Z", imoveis.get(0).getCreatedAt());
	}
}
