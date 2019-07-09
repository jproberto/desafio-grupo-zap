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
 * Classe de serviço que recebe as chamadas feitas por {@link VivarealController}. Implementa {@link ImovelService#getFromCache()} com as regras definidas para que um imóvel seja elegido para o Vivareal. 
 */
@Service
public class VivarealService extends ImovelService {
	private Logger logger = LoggerFactory.getLogger(VivarealService.class);

	@Override
	protected List<Imovel> getFromCache() throws JsonToImoveisException {
		List<Imovel> imoveis;

		// Se o cache estiver preenchido, usa-se ele
		if (ImovelCache.vivarealHasValues()) {
			imoveis = ImovelCache.getVivarealImoveis();
		} else {
			logger.info("Filtrando imóveis para Vivareal...");
			
			// Se não estiver, busca os valores do source, aplica os filtros e atualiza o cache
			imoveis = new ArrayList<Imovel>();

			List<Imovel> imoveisSource = consumerService.getImoveis();
			
			//Definindo os valores padrão para os campos a serem comparados
			double maximumRentDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.VIVAREAL_MAXIMUM_RENT_VALUE);
			double maximumSaleDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.VIVAREAL_MAXIMUM_SALE_VALUE);
			double maximumCondoFeeDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.VIVAREAL_MAXIMUM_CONDO_FEE_PERCENT) / 100;
			
			//Percentual aplicado caso o imóvel esteja dentro do bounding box dos arredores do Grupo Zap
			double boudingboxRulePercent = 1 + (PropertiesHandler.getDouble(PropertiesKeys.ZAP_BOUDING_BOX_RULE_PERCENT) / 100);
			
			logger.info("Valor máximo de aluguel padrão: " + maximumRentDefaultValue);
			logger.info("Valor máximo de venda padrão: " + maximumSaleDefaultValue);
			logger.info("Valor máximo de condomínio padrão: " + maximumCondoFeeDefaultValue);
			logger.info("Percentual de modificação caso esteja dentro do bouding box dos arredores do GrupoZap: " + boudingboxRulePercent + "%");
			
			double maximumRentValue, maximumSaleValue, maximumCondoFeePercentValue;
			
			for (Imovel imovel : imoveisSource) {
				//Se o imóvel está dentro do bounding box dos arredores do Grupo Zap, aplica a porcentagem
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
				
				//Se o imóvel estiver a venda, verifica se o valor está abaixo do máximo permitido
				if (imovel.getPricingInfos().getBusinessType().contains("SALE")) {
					logger.info("Imóvel a venda.");
					
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
				
				//Se o imóvel está para alugar, verifica se o valor do aluguel está abaixo do limite máximo permitido e se o valor do condmínio respeita a porcentagem do aluguel definida.
				if (imovel.getPricingInfos().getBusinessType().contains("RENTAL")) {
					logger.info("Imóvel para alugar.");
					
					if (imovel.getPricingInfos().getRentalTotalPrice() <= maximumRentValue) {
						//Apenas para imóveis com valor de condomínio válido
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
			
			//Após aplicar as regras, atualiza o cache correspondente
			ImovelCache.updateVivarealImoveis(imoveis);
		}
		
		return imoveis;
	}
}
