package br.com.jproberto.desafioGrupoZap;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import br.com.jproberto.desafioGrupoZap.core.service.VivarealService;
import br.com.jproberto.desafioGrupoZap.core.service.ZapService;
import br.com.jproberto.desafioGrupoZap.exception.JsonToImoveisException;
import br.com.jproberto.desafioGrupoZap.util.PropertiesHandler;
import br.com.jproberto.desafioGrupoZap.util.PropertiesKeys;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppConfig.class)
public class ImovelServiceTest {
	
	@Autowired
	private ZapService zap;
	
	@Autowired
	private VivarealService vivareal;

	@Test
	public void testGetZapImoveis() throws JsonToImoveisException {
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
			
			List<Imovel> imoveis = zap.getImoveis(currentPage, pageSize);
			
			assertThat(pageSize).isEqualTo(imoveis.size());
			
			Imovel imovel = imoveis.get(index);
			
			if (imovel.isInGrupoZapBoudingBox()) {
				minimumRentDefaultValue *= boudingboxRulePercent;
				minimumSaleDefaultValue *= boudingboxRulePercent;
				minimumSquareMeterDefaultValue *= boudingboxRulePercent;
			}
			
			if (imovel.getPricingInfos().getBusinessType().contains("RENTAL")) {
				assertThat(imovel.getPricingInfos().getRentalTotalPrice()).isGreaterThanOrEqualTo(minimumRentDefaultValue);
			}
			
			if (imovel.getPricingInfos().getBusinessType().contains("SALE")) {
				assertThat(imovel.getPricingInfos().getPrice()).isGreaterThanOrEqualTo(minimumSaleDefaultValue);
				
				if (imovel.getUsableAreas() > 0) {
					double squareMeterValue = imovel.getPricingInfos().getPrice() / imovel.getUsableAreas();

					assertThat(squareMeterValue).isGreaterThan(minimumSquareMeterDefaultValue);
				}
			}
		}
	}
	
	@Test
	public void testGetVivarealImoveis() throws JsonToImoveisException {
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
			
			List<Imovel> imoveis = vivareal.getImoveis(currentPage, pageSize);
			
			assertThat(pageSize).isEqualTo(imoveis.size());
			
			Imovel imovel = imoveis.get(index);
			
			if (imovel.isInGrupoZapBoudingBox()) {
				maximumRentDefaultValue *= boudingboxRulePercent;
				maximumSaleDefaultValue *= boudingboxRulePercent;
				maximumCondoFeeDefaultValue *= boudingboxRulePercent;
			}
			
			if (imovel.getPricingInfos().getBusinessType().contains("SALE")) {
				assertThat(imovel.getPricingInfos().getPrice() <= maximumSaleDefaultValue);
			}
			
			if (imovel.getPricingInfos().getBusinessType().contains("RENTAL")) {
				assertThat(imovel.getPricingInfos().getRentalTotalPrice()).isLessThanOrEqualTo(maximumRentDefaultValue);
				
				if (imovel.getPricingInfos().getMonthlyCondoFee() > 0) {
					double maximumCondoFeeValue = imovel.getPricingInfos().getRentalTotalPrice() * maximumCondoFeeDefaultValue;
					
					assertThat(imovel.getPricingInfos().getMonthlyCondoFee()).isLessThan(maximumCondoFeeValue);
				}
			}
		}
	}
	
}
