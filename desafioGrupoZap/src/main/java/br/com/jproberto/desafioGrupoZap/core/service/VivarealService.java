package br.com.jproberto.desafioGrupoZap.core.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.jproberto.desafioGrupoZap.consumer.cache.ImovelCache;
import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import br.com.jproberto.desafioGrupoZap.util.PropertiesHandler;
import br.com.jproberto.desafioGrupoZap.util.PropertiesKeys;

@Service
public class VivarealService extends ImovelService {
	private Logger logger = LoggerFactory.getLogger(VivarealService.class);

	@Override
	protected List<Imovel> getFromCache() {
		List<Imovel> imoveis;

		// Se o cache estiver preenchido, usamos ele
		if (ImovelCache.vivarealHasValues()) {
			imoveis = ImovelCache.getImoveis();
		} else {
			logger.info("Filtrando imóveis para Vivareal...");
			
			// Se não estiver pegamos do source, aplicamos os filtros e atualizamos o cache
			imoveis = new ArrayList<Imovel>();

			List<Imovel> imoveisSource = consumerService.getImoveis();
			
			double maximumRentDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.VIVAREAL_MAXIMUM_RENT_VALUE);
			double maximumSaleDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.VIVAREAL_MAXIMUM_SALE_VALUE);
			double maximumCondoFeeDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.VIVAREAL_MAXIMUM_CONDO_FEE_PERCENT) / 100;
			
			double boudingboxRulePercent = 1 + (PropertiesHandler.getDouble(PropertiesKeys.ZAP_BOUDING_BOX_RULE_PERCENT) / 100);
			
			logger.info("Valor máximo de aluguel padrão: " + maximumRentDefaultValue);
			logger.info("Valor máximo de venda padrão: " + maximumSaleDefaultValue);
			logger.info("Valor máximo de condomínio padrão: " + maximumCondoFeeDefaultValue);
			logger.info("Percentual de modificação caso esteja dentro do bouding box dos arredores do GrupoZap: " + boudingboxRulePercent + "%");
			
			double maximumRentValue, maximumSaleValue, maximumCondoFeePercentValue;
			
			for (Imovel imovel : imoveisSource) {
				if (imovel.isInGrupoZapBoudingBox()) {
					logger.info("Imóvel está dentro do bouding box dos arredores do GrupoZap. Percentual de alteração aplicado.");
					
					maximumRentValue = maximumRentDefaultValue * boudingboxRulePercent;
					maximumSaleValue = maximumSaleDefaultValue * boudingboxRulePercent;
					maximumCondoFeePercentValue = maximumCondoFeeDefaultValue * boudingboxRulePercent;
				} else {
					maximumRentValue = maximumRentDefaultValue;
					maximumSaleValue = maximumSaleDefaultValue;
					maximumCondoFeePercentValue = maximumCondoFeeDefaultValue;
				}
				
				if (imovel.getPricingInfos().getBusinessType().contains("SALE")) {
					logger.info("Imóvel a venda.");
					
					//Verifica o valor máximo de venda
					if (imovel.getPricingInfos().getPrice() <= maximumSaleValue) {
						logger.info("Valor de venda abaixo do limite máximo.");
						logger.info("Imóvel adicionado à lista do Vivareal.");
						
						imoveis.add(imovel);
						continue;
					} else {
						logger.info("Valor de venda acima do limite máximo.");
						logger.info("Imóvel não foi adicionado à lista do Vivareal.");
					}
				}
				
				if (imovel.getPricingInfos().getBusinessType().contains("RENTAL")) {
					logger.info("Imóvel para alugar.");
					
					//Verfica o valor máximo de aluguel
					if (imovel.getPricingInfos().getRentalTotalPrice() <= maximumRentValue) {
						//Verifica o valor máximo do condomínio, apenas para imóveis com valor de condomínio válido
						logger.info("Valor de aluguel abaixo do limite máximo.");
						
						if (imovel.getPricingInfos().getMonthlyCondoFee() > 0) {
							logger.info("Valor de condomínio válido.");
							double maximumCondoFeeValue = imovel.getPricingInfos().getRentalTotalPrice() * maximumCondoFeePercentValue;
							
							if (imovel.getPricingInfos().getMonthlyCondoFee() < maximumCondoFeeValue) {
								logger.info("Valor de condomínio abaixo do limite máximo.");
								logger.info("Imóvel adicionado à lista do Vivareal.");
								
								imoveis.add(imovel);
								continue;
							} else {
								logger.info("Valor de condomínio acima do limite máximo.");
								logger.info("Imóvel adicionado à lista do Vivareal.");
							}
						} else {
							//Se o valor do condomínio não for válido essa regra é ignorada
							logger.info("Valor de condomínio não é válido.");
							logger.info("Imóvel adicionado à lista do Vivareal.");
							
							imoveis.add(imovel);
							continue;
						}
					} else {
						logger.info("Valor de aluguel acima do limite máximo.");
						logger.info("Imóvel não foi adicionado à lista do Vivareal.");
					}
				}
			}
			
			ImovelCache.updateVivarealImoveis(imoveis);
		}
		
		return imoveis;
	}
}
