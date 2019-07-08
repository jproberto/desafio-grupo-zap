package br.com.jproberto.desafioGrupoZap;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import br.com.jproberto.desafioGrupoZap.consumer.cache.ImovelCache;
import br.com.jproberto.desafioGrupoZap.core.controller.VivarealController;
import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import br.com.jproberto.desafioGrupoZap.core.model.Response;
import br.com.jproberto.desafioGrupoZap.util.PropertiesHandler;
import br.com.jproberto.desafioGrupoZap.util.PropertiesKeys;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class VivarealControllerTest extends TestCase {
	
	@Autowired
	private VivarealController vivareal;
	
	public static Test suite() {
		return new TestSuite(ConsumerServiceTest.class);
	}
	
	public void testGetVivarealImoveis() {
		Random r = new Random();
		
		int tries = r.nextInt(10) + 1;
		int pageSize, currentPage, index;
		
		double maximumRentDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.VIVAREAL_MAXIMUM_RENT_VALUE);
		double maximumSaleDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.VIVAREAL_MAXIMUM_SALE_VALUE);
		double maximumCondoFeeDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.VIVAREAL_MAXIMUM_CONDO_FEE_PERCENT) / 100;
		
		double boudingboxRulePercent = 1 + (PropertiesHandler.getDouble(PropertiesKeys.ZAP_BOUDING_BOX_RULE_PERCENT) / 100);
		
		for (int i = 0; i < tries; i++) {
			pageSize = r.nextInt(10) + 10;
			currentPage = r.nextInt(100) + 1;
			index = r.nextInt(pageSize);
			
			ResponseEntity<Response> responseEntity = vivareal.getImoveis(String.valueOf(currentPage), String.valueOf(pageSize));
			Response response = responseEntity.getBody();
			
			assertEquals(currentPage, response.getPageNumber());
			assertEquals(pageSize, response.getPageSize());
			assertEquals(pageSize, response.getListings().size());
			assertEquals(ImovelCache.getTotalZapCount(), response.getTotalCount());
			
			Imovel imovel = response.getListings().get(index);
			
			if (imovel.isInGrupoZapBoudingBox()) {
				maximumRentDefaultValue *= boudingboxRulePercent;
				maximumSaleDefaultValue *= boudingboxRulePercent;
				maximumCondoFeeDefaultValue *= boudingboxRulePercent;
			}
			
			if (imovel.getPricingInfos().getBusinessType().contains("SALE")) {
				assertTrue(imovel.getPricingInfos().getPrice() <= maximumSaleDefaultValue);
			}
			
			if (imovel.getPricingInfos().getBusinessType().contains("RENTAL")) {
				assertTrue(imovel.getPricingInfos().getRentalTotalPrice() <= maximumRentDefaultValue);
				
				if (imovel.getPricingInfos().getMonthlyCondoFee() > 0) {
					double maximumCondoFeeValue = imovel.getPricingInfos().getRentalTotalPrice() * maximumCondoFeeDefaultValue;
					assertTrue(imovel.getPricingInfos().getMonthlyCondoFee() < maximumCondoFeeValue);
				}
			}
		}
	}
}
