### Desafio Grupo Zap

Esse projeto foi desenvolvido por João Paulo de Souza como desafio para processo seletivo do Grupo Zap.

=============

 - ##### Como rodar localmente?
 O projeto utiliza Spring Boot e sua classe principal é a **AppConfig**, que pode ser encontrada no seguinte caminho: 
 > /desafioGrupoZap/src/main/java/br/com/jproberto/desafioGrupoZap/
 
 - ##### Como testar?
 Foram criadas três classes de teste: **ConsumerServiceTest, ImovelServiceTest e PropertiesTest**. Estão localizadas no seguinte pacote:
 > /desafioGrupoZap/src/test/java/br/com/jproberto/desafioGrupoZap/
 
 - ##### Como fazer o deploy?
 Por usar Spring Boot, basta gerar um jar executável usando a classe **AppConfig** como configuração de lançamento. O arquivo **application.properties** define algumas propriedades que podem ser alteradas caso os valores de comparação mudem, sem que seja necessário fazer um novo deploy da aplicação.
 
 - ##### Endpoints
 | Endpoint | Método | Ação |
 | -------- | ------- | ---- |
 | /vivareal | GET | Retorna a lista de imóveis que atendem as regras do Vivareal. |
 | /zap | GET | Retorna a lista de imóveis que atendem as regras do Zap. |
 
 Ambos os endpoints são paginados e permitem navegação pelas páginas como uso do parâmetro "page". Também permitem alteração do tamanho da página retornada com o parâmetro "pageSize". As informações de paginação são exibidas nos metadados.
 
 - ##### Exemplo de resultado:
 ```
 {  
   "pageNumber":1,
   "pageSize":1,
   "totalCount":4074,
   "listings":[  
      {  
         "usableAreas":77,
         "listingType":"USED",
         "createdAt":"2018-05-08T00:29:38.179Z",
         "listingStatus":"ACTIVE",
         "id":"fed26dbe5881",
         "parkingSpaces":1,
         "updatedAt":"2018-05-08T00:29:38.179Z",
         "owner":false,
         "images":[  
            "https://resizedimgs.vivareal.com/crop/400x300/vr.images.sp/76a9e6394825a55244e77df2acc2478f.jpg",
            "https://resizedimgs.vivareal.com/crop/400x300/vr.images.sp/bf6583b1f9b624f391fc433eda8090d5.jpg",
            "https://resizedimgs.vivareal.com/crop/400x300/vr.images.sp/2039fab0476b6e5cf17dd3132922f326.jpg",
            "https://resizedimgs.vivareal.com/crop/400x300/vr.images.sp/3f428dea9ff9903d88e62694cdc3e282.jpg",
            "https://resizedimgs.vivareal.com/crop/400x300/vr.images.sp/07aa0b09bffbd51cca83f3f76c1483c2.jpg"
         ],
         "address":{  
            "city":"SÃ£o Paulo",
            "neighborhood":"Campo Belo",
            "geoLocation":{  
               "precision":"ROOFTOP",
               "location":{  
                  "lon":-46.672953,
                  "lat":-23.622739,
                  "inGrupoZapBoudingBox":false
               },
               "inGrupoZapBoudingBox":false
            },
            "inGrupoZapBoudingBox":false
         },
         "bathrooms":3,
         "bedrooms":3,
         "pricingInfos":{  
            "period":"MONTHLY",
            "yearlyIptu":810,
            "price":3500,
            "rentalTotalPrice":4440,
            "businessType":"RENTAL",
            "monthlyCondoFee":940
         },
         "inGrupoZapBoudingBox":false
      }
   ],
}
```