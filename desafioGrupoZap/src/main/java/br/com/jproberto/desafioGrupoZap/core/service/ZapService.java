package br.com.jproberto.desafioGrupoZap.core.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.jproberto.desafioGrupoZap.consumer.cache.ImovelCache;
import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import br.com.jproberto.desafioGrupoZap.exception.JsonToImoveisException;
import br.com.jproberto.desafioGrupoZap.util.PropertiesHandler;
import br.com.jproberto.desafioGrupoZap.util.PropertiesKeys;

/**
 * Classe de serviço que recebe as chamadas feitas por {@link ZapController}. Implementa {@link ImovelService#getFromCache()} com as regras definidas para que um imóvel seja elegido para o Zap. 
 */
@Service
public class ZapService extends ImovelService {
	private Logger logger = LoggerFactory.getLogger(ZapService.class);

	protected List<Imovel> getFromCache() throws JsonToImoveisException {
		List<Imovel> imoveis;
		
		// Se o cache estiver preenchido, usa-se ele
		if (ImovelCache.zapHasValues()) {
			imoveis = ImovelCache.getImoveis();
		} else {
			logger.info("Filtrando imóveis para Zap...");
			
			// Se não estiver, busca os valores do source, aplica os filtros e atualiza o cache
			imoveis = new ArrayList<Imovel>();

			List<Imovel> imoveisSource = consumerService.getImoveis();

			//Definindo os valores padrão para os campos a serem comparados
			double minimumRentDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.ZAP_MINIMUM_RENT_VALUE);
			double minimumSaleDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.ZAP_MINIMUM_SALE_VALUE);
			double minimumSquareMeterDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.ZAP_MINIMUM_SQUARE_METER_VALUE);

			//Percentual aplicado caso o imóvel esteja dentro do bounding box dos arredores do Grupo Zap
			double boudingboxRulePercent = 1 - (PropertiesHandler.getDouble(PropertiesKeys.ZAP_BOUDING_BOX_RULE_PERCENT) / 100);
			
			logger.info("Valor mínimo de alguel padrão: " + minimumRentDefaultValue);
			logger.info("Valor mínimo de venda padrão: " + minimumSaleDefaultValue);
			logger.info("Valor mínimo de metro quadrado padrão: " + minimumSquareMeterDefaultValue);
			logger.info("Percentual de modificação caso esteja dentro do bouding box dos arredores do GrupoZap: " + boudingboxRulePercent + "%");
			
			double minimumRentValue, minimumSaleValue, minimumSquareMeterValue;

			for (Imovel imovel : imoveisSource) {
				//Se o imóvel está dentro do bounding box dos arredores do Grupo Zap, aplica a porcentagem
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

				//Se o imóvel estiver para alugar, verifica se o valor do aluguel está acima do limite mínimo permitido
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

				//Se o imóvel estiver a venda, verifica se o valor da venda e o valor do metro quadrado estão acima do valor mínimo permitido
				if (imovel.getPricingInfos().getBusinessType().contains("SALE")) {
					logger.info("Imóvel a venda.");
					
					if (imovel.getPricingInfos().getPrice() >= minimumSaleValue) {
						logger.info("Valor de venda acima do limite mínimo.");
						
						//Apenas para imóveis com valor de área útil válido
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

			//Após aplicar as regras, atualiza o cache correspondente
			ImovelCache.updateZapImoveis(imoveis);
		}
		
		return imoveis;
	}

}
