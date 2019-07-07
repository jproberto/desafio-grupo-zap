package br.com.jproberto.desafioGrupoZap.consumer.service;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import br.com.jproberto.desafioGrupoZap.consumer.cache.ImovelCache;
import br.com.jproberto.desafioGrupoZap.core.model.Imovel;
import br.com.jproberto.desafioGrupoZap.util.PropertiesHandler;
import br.com.jproberto.desafioGrupoZap.util.PropertiesKeys;

@Service
public class ConsumerService {
	private final RestTemplate restTemplate = new RestTemplate();

	private final String url = PropertiesHandler.getString(PropertiesKeys.API_SOURCE_IMOVEIS);

	/**
	 * Monta o Header a ser usado na requisição enviada à API
	 * 
	 * @return
	 */
	private HttpEntity<String> getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("user-agent", "desafio-grupo-zap");
		return new HttpEntity<String>("parameters", headers);
	}

	/**
	 * Chamada feita a URL com o source para buscar os imoveis
	 * 
	 * @return representação envelopada dos imóveis
	 * @throws Exception
	 */
	public List<Imovel> getImoveis() {
		//Se o cache tiver valor, trabalhamos com ele
		if (ImovelCache.hasValues()) {
			return ImovelCache.getImoveis();
		}
		
		//O cache é esvaziado de tempos em tempos, então uma nova chamada é necessária para termos resultados atualizados
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);

		List<Imovel> imoveis = getFromJson(response.getBody());
		
		Predicate<Imovel> latitudeLongitudeZero = imovel -> imovel.getAddress().getGeoLocation().getLocation().getLat() == 0.0 && imovel.getAddress().getGeoLocation().getLocation().getLon() == 0.0;
		imoveis.removeIf(latitudeLongitudeZero);
		
		ImovelCache.updateImoveis(imoveis);
		return imoveis;
	}

	/**
	 * Transforma uma String que represente um JSON num objeto envelopado que
	 * corresponde ao recurso desejado
	 * 
	 * @param json
	 *            String que representa o JSON retornado pela consulta feita ao
	 *            source
	 * @return objeto envelopado do recurso
	 * @throws Exception
	 */
	private List<Imovel> getFromJson(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			TypeFactory typeFactory = mapper.getTypeFactory();
			CollectionType collectionType = typeFactory.constructCollectionType(List.class, Imovel.class);
			return mapper.readValue(json, collectionType);
		} catch (Exception e) {
			// TODO lançar exceção mais amigável
			e.printStackTrace();
		}
		return null;
	}
}
