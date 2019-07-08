package br.com.jproberto.desafioGrupoZap;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import br.com.jproberto.desafioGrupoZap.consumer.cache.ImovelCache;
import br.com.jproberto.desafioGrupoZap.core.controller.ZapController;
import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import br.com.jproberto.desafioGrupoZap.core.model.Response;
import br.com.jproberto.desafioGrupoZap.util.PropertiesHandler;
import br.com.jproberto.desafioGrupoZap.util.PropertiesKeys;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ZapControllerTest  extends TestCase {
	@Autowired
	private ZapController zap;
	
	public static Test suite() {
		return new TestSuite(ConsumerServiceTest.class);
	}

	public void testGetZapImoveis() {
		Random r = new Random();
		
		int tries = r.nextInt(10) + 1;
		int pageSize, currentPage, index;
		
		double minimumRentDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.ZAP_MINIMUM_RENT_VALUE);
		double minimumSaleDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.ZAP_MINIMUM_SALE_VALUE);
		double minimumSquareMeterDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.ZAP_MINIMUM_SQUARE_METER_VALUE);
		
		double boudingboxRulePercent = 1 - (PropertiesHandler.getDouble(PropertiesKeys.ZAP_BOUDING_BOX_RULE_PERCENT) / 100);
		
		for (int i = 0; i < tries; i++) {
			pageSize = r.nextInt(10) + 10;
			currentPage = r.nextInt(100) + 1;
			index = r.nextInt(pageSize);
			
			ResponseEntity<Response> responseEntity = zap.getImoveis(String.valueOf(currentPage), String.valueOf(pageSize));
			Response response = responseEntity.getBody();
			
			assertEquals(currentPage, response.getPageNumber());
			assertEquals(pageSize, response.getPageSize());
			assertEquals(pageSize, response.getListings().size());
			assertEquals(ImovelCache.getTotalZapCount(), response.getTotalCount());
			
			Imovel imovel = response.getListings().get(index);
			
			if (imovel.isInGrupoZapBoudingBox()) {
				minimumRentDefaultValue *= boudingboxRulePercent;
				minimumSaleDefaultValue *= boudingboxRulePercent;
				minimumSquareMeterDefaultValue *= boudingboxRulePercent;
			}
			
			if (imovel.getPricingInfos().getBusinessType().contains("RENTAL")) {
				assertTrue(imovel.getPricingInfos().getRentalTotalPrice() >= minimumRentDefaultValue);
			}
			
			if (imovel.getPricingInfos().getBusinessType().contains("SALE")) {
				assertTrue(imovel.getPricingInfos().getPrice() >= minimumSaleDefaultValue);
				
				if (imovel.getUsableAreas() > 0) {
					double squareMeterValue = imovel.getPricingInfos().getPrice() / imovel.getUsableAreas();

					assertTrue(squareMeterValue > minimumSquareMeterDefaultValue);
				}
			}
		}
	}
	
}
