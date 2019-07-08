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
public class ZapService extends ImovelService {
	private Logger logger = LoggerFactory.getLogger(ZapService.class);


	protected List<Imovel> getFromCache() {
		List<Imovel> imoveis;
		
		// Se o cache estiver preenchido, usamos ele
		if (ImovelCache.zapHasValues()) {
			imoveis = ImovelCache.getImoveis();
		} else {
			logger.info("Filtrando imóveis para Zap...");
			
			// Se não estiver pegamos do source, aplicamos os filtros e atualizamos o cache
			imoveis = new ArrayList<Imovel>();

			List<Imovel> imoveisSource = consumerService.getImoveis();

			double minimumRentDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.ZAP_MINIMUM_RENT_VALUE);
			double minimumSaleDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.ZAP_MINIMUM_SALE_VALUE);
			double minimumSquareMeterDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.ZAP_MINIMUM_SQUARE_METER_VALUE);

			double boudingboxRulePercent = 1 - (PropertiesHandler.getDouble(PropertiesKeys.ZAP_BOUDING_BOX_RULE_PERCENT) / 100);
			
			logger.info("Valor mínimo de alguel padrão: " + minimumRentDefaultValue);
			logger.info("Valor mínimo de venda padrão: " + minimumSaleDefaultValue);
			logger.info("Valor mínimo de metro quadrado padrão: " + minimumSquareMeterDefaultValue);
			logger.info("Percentual de modificação caso esteja dentro do bouding box dos arredores do GrupoZap: " + boudingboxRulePercent + "%");
			
			double minimumRentValue, minimumSaleValue, minimumSquareMeterValue;

			for (Imovel imovel : imoveisSource) {
				if (imovel.isInGrupoZapBoudingBox()) {
					logger.info("Imóvel está dentro do bouding box dos arredores do GrupoZap. Percentual de alteração aplicado.");
					
					minimumRentValue = minimumRentDefaultValue * boudingboxRulePercent;
					minimumSaleValue = minimumSaleDefaultValue * boudingboxRulePercent;
					minimumSquareMeterValue = minimumSquareMeterDefaultValue * boudingboxRulePercent;
				} else {
					minimumRentValue = minimumRentDefaultValue;
					minimumSaleValue = minimumSaleDefaultValue;
					minimumSquareMeterValue = minimumSquareMeterDefaultValue;
				}

				//Verifica o valor mínimo de aluguel
				if (imovel.getPricingInfos().getBusinessType().contains("RENTAL")) {
					logger.info("Imóvel para alugar.");
					
					if (imovel.getPricingInfos().getRentalTotalPrice() >= minimumRentValue) {
						logger.info("Valor de aluguel acima do limite mínimo.");
						logger.info("Imóvel adicionado à lista do Zap.");
						
						imoveis.add(imovel);
						continue;
					} else {
						logger.info("Valor de aluguel abaixo do limite mínimo.");
						logger.info("Imóvel não foi adicionado à lista do Zap.");
					}
				}

				if (imovel.getPricingInfos().getBusinessType().contains("SALE")) {
					logger.info("Imóvel a venda.");
					
					//Verifica o valor mínimo de venda
					if (imovel.getPricingInfos().getPrice() >= minimumSaleValue) {
						logger.info("Valor de venda acima do limite mínimo.");
						
						//Verifica o valor mínimo do metro quadrado, apenas para imóveis com valor de área útil válido
						if (imovel.getUsableAreas() > 0) {
							logger.info("Área útil válida.");
							
							double squareMeterValue = imovel.getPricingInfos().getPrice() / imovel.getUsableAreas();

							if (squareMeterValue > minimumSquareMeterValue) {
								logger.info("Valor de metro quadrado acima do limite mínimo.");
								logger.info("Imóvel adicionado à lista do Zap.");
								
								imoveis.add(imovel);
								continue;
							} else {
								logger.info("Valor de metro quadrado abaixo do limite mínimo.");
								logger.info("Imóvel não foi adicionado à lista do Zap.");
							}
						} else {
							//Se o valor da área útil não for válido essa regra é ignorada
							logger.info("Área útil não é válida.");
							logger.info("Imóvel adicionado à lista do Zap.");
							
							imoveis.add(imovel);
							continue;
						}
					} else {
						logger.info("Valor de venda abaixo do limite mínimo.");
						logger.info("Imóvel não foi adicionado à lista do Zap.");
					}
				}
			}

			ImovelCache.updateZapImoveis(imoveis);
		}
		
		return imoveis;
	}

}
