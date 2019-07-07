package br.com.jproberto.desafioGrupoZap.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jproberto.desafioGrupoZap.consumer.cache.ImovelCache;
import br.com.jproberto.desafioGrupoZap.consumer.service.ConsumerService;
import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import br.com.jproberto.desafioGrupoZap.util.Pageable;
import br.com.jproberto.desafioGrupoZap.util.PropertiesHandler;
import br.com.jproberto.desafioGrupoZap.util.PropertiesKeys;

@Service
public class ZapService {

	@Autowired
	private ConsumerService consumerService;
	
	private Pageable<Imovel> paginacao;

	public List<Imovel> getImoveis(int currentPage) {
		return getImoveis(currentPage, 0);
	}
	
	public List<Imovel> getImoveis(int currentPage, int pageSize) {
		if (paginacao == null) {
			List<Imovel> imoveis = getFromCache();
			paginacao = new Pageable<Imovel>(imoveis);
		}
		
		paginacao.setCurrentPage(currentPage);
		
		if (pageSize > 0) {
			paginacao.setPageSize(pageSize);
		}
		
		return paginacao.getListForCurrentPage();
	}

	private List<Imovel> getFromCache() {
		List<Imovel> imoveis;
		
		// Se o cache estiver preenchido, usamos ele
		if (ImovelCache.zapHasValues()) {
			imoveis = ImovelCache.getImoveis();
		} else {
			// Se não estiver pegamos do source, aplicamos os filtros e atualizamos o cache
			imoveis = new ArrayList<Imovel>();

			List<Imovel> imoveisSource = consumerService.getImoveis();

			double minimumRentDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.ZAP_MINIMUM_RENT_VALUE);
			double minimumSaleDefaultValue = PropertiesHandler.getDouble(PropertiesKeys.ZAP_MINIMUM_SALE_VALUE);
			double minimumSquareMeterDefaultValue = PropertiesHandler
					.getDouble(PropertiesKeys.ZAP_MINIMUM_SQUARE_METER_VALUE);

			double boudingboxRulePercent = 1
					- (PropertiesHandler.getDouble(PropertiesKeys.ZAP_BOUDING_BOX_RULE_PERCENT) / 100);

			double minimumRentValue, minimumSaleValue, minimumSquareMeterValue;

			for (Imovel imovel : imoveisSource) {
				if (imovel.isInGrupoZapBoudingBox()) {
					minimumRentValue = minimumRentDefaultValue * boudingboxRulePercent;
					minimumSaleValue = minimumSaleDefaultValue * boudingboxRulePercent;
					minimumSquareMeterValue = minimumSquareMeterDefaultValue * boudingboxRulePercent;
				} else {
					minimumRentValue = minimumRentDefaultValue;
					minimumSaleValue = minimumSaleDefaultValue;
					minimumSquareMeterValue = minimumSquareMeterDefaultValue;
				}

				// Se for aluguel, valor mínimo de 3.500
				if (imovel.getPricingInfos().getBusinessType().contains("RENTAL")) {
					if (imovel.getPricingInfos().getRentalTotalPrice() >= minimumRentValue) {
						imoveis.add(imovel);
						continue;
					}
				}

				if (imovel.getPricingInfos().getBusinessType().contains("SALE")) {
					// Se for venda, valor mínimo de 600.000
					if (imovel.getPricingInfos().getPrice() >= minimumSaleValue) {
						// valor do metro quadrado deve ser maior que 3.500
						if (imovel.getUsableAreas() > 0) {
							double squareMeterValue = imovel.getPricingInfos().getPrice() / imovel.getUsableAreas();

							if (squareMeterValue > minimumSquareMeterValue) {
								imoveis.add(imovel);
								continue;
							}
						} else {
							// Apenas para imóveis com área de uso maior que 0
							imoveis.add(imovel);
							continue;
						}
					}
				}
			}

			ImovelCache.updateZapImoveis(imoveis);
		}
		
		return imoveis;
	}

}
