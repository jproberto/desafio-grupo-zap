package br.com.jproberto.desafioGrupoZap.consumer.service;

import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import br.com.jproberto.desafioGrupoZap.exception.JsonToImoveisException;
import br.com.jproberto.desafioGrupoZap.util.PropertiesHandler;
import br.com.jproberto.desafioGrupoZap.util.PropertiesKeys;

/**
 *	Serviço que cuida da chamada ao source externo e transformação da resposta em objetos com significado 
 *
 */
@Service
public class ConsumerService {
	private final RestTemplate restTemplate = new RestTemplate();

	private final String url = PropertiesHandler.getString(PropertiesKeys.API_SOURCE_IMOVEIS);
	
	private Logger logger = LoggerFactory.getLogger(ConsumerService.class);

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
	 * @throws JsonToImoveisException 
	 */
	public List<Imovel> getImoveis() throws JsonToImoveisException {
		//Se o cache tiver valor, trabalhamos com ele
		if (ImovelCache.hasValues()) {
			return ImovelCache.getImoveis();
		}
		
		logger.info("Buscando informação de source externo...");
		
		//O cache é esvaziado de tempos em tempos, então uma nova chamada é necessária para termos resultados atualizados
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, getHeaders(), String.class);
		
		logger.info("Imóveis retornados.");
		logger.info("Transformando json em objetos...");

		List<Imovel> imoveis = getFromJson(response.getBody());
		
		logger.info("Objetos transformados.");
		logger.info("Removendo imóveis com latitude e longitude inválidas...");
		
		//Imóveis com latitude e longitude são iguais a zero não são elegíveis para nenhum sistema e por isso são removidos do resultado
		Predicate<Imovel> latitudeLongitudeZero = imovel -> imovel.getAddress().getGeoLocation().getLocation().getLat() == 0.0 && imovel.getAddress().getGeoLocation().getLocation().getLon() == 0.0;
		imoveis.removeIf(latitudeLongitudeZero);
		
		logger.info("Imóveis removidos.");
		
		//Após a busca e tratamento, o cache de imóveis é atualizado
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
	 * @throws JsonToImoveisException 
	 */
	private List<Imovel> getFromJson(String json) throws JsonToImoveisException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			TypeFactory typeFactory = mapper.getTypeFactory();
			CollectionType collectionType = typeFactory.constructCollectionType(List.class, Imovel.class);
			return mapper.readValue(json, collectionType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JsonToImoveisException();
		}
	}
}
