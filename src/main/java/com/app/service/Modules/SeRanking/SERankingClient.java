package com.app.service.Modules.SeRanking;

import com.app.service.Modules.SeRanking.Class.MyResponseClass;
import com.app.service.Modules.keywords.types.keyworkData;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SERankingClient {

    @Value("${seranking.api.key}")
    private final String apiKey;
    private final RestTemplate restTemplate;

    private final String baseUrl= "https://api4.seranking.com/research";

    public SERankingClient(String apiKey){
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
    }
    public MyResponseClass getSeRankingDomainHistory(String domain) {
        return getSeRankingDomainHistory(domain, "es", "organic");
    }

    public MyResponseClass getSeRankingDomainHistory(String domain, String source, String type){
        String endpoint = "/" + source + "/overview/history";

        String url = baseUrl + endpoint + "?domain=" + domain + "&type=" + type;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token " + apiKey);
        headers.add("Content-Type", "application/json; charset=utf-8");

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        if (response.getStatusCodeValue() == 200) {
            String jsonResponse = response.getBody();

            // Aquí convertimos la respuesta JSON en una instancia de MyResponseClass
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                MyResponseClass responseClass = objectMapper.readValue(jsonResponse, MyResponseClass.class);
                return responseClass;
            } catch (JsonProcessingException e) {
                // Manejar excepción en caso de error de mapeo JSON
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Error al hacer la solicitud: " + response.getBody());
    }

    public Object getSeRankingDomainKeywords(String domain){
        return getSeRankingDomainKeywords(domain, "es", 10, 1);
    }

    public Object getSeRankingDomainKeywords (String domain, String source, int limit, int page){
        String endpoint = this.baseUrl+"/"+ source + "/keywords";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token " + apiKey);
        headers.add("Content-Type", "application/json; charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<Object> response = new RestTemplate().exchange(
                endpoint + "?domain=" + domain + "&limit=" + limit + "&page=" + page,
                HttpMethod.GET,
                entity,
                Object.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException(response.getBody().toString());
        }
    }

    public Object getSERankingDomainCompeitors(String domain){
        return getSERankingDomainCompeitors(domain, "es", "organic", 1);
    }

    public Object getSERankingDomainCompeitors(String domain, String source, String competitorsType, int status){
        String endpoint = this.baseUrl + "/" + source + "/competitors/";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token " + apiKey);
        headers.add("Content-Type", "application/json; charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("domain", domain);
        params.add("type", competitorsType);
        params.add("stats", String.valueOf(status));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<Object> response = new RestTemplate().exchange(
                endpoint,
                HttpMethod.GET,
                entity,
                Object.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            Object responseBody = response.getBody();

            if (responseBody instanceof List<?>) {
                List<Object> responseList = (List<Object>) responseBody;
                for (Object domainData : responseList) {
                    Map<String, Object> domainMap = (Map<String, Object>) domainData;
                    int commonKeywords = (int) domainMap.get("common_keywords");
                    int totalKeywords = (int) domainMap.get("total_keywords");
                    double similarity = (double) Math.round(((double) commonKeywords / totalKeywords) * 100.0 * 100) / 100.0;
                    domainMap.put("similarity", similarity);
                }
            }

            return responseBody;
        } else {
            throw new RuntimeException(response.getBody().toString());
        }
    }

    public Object getSERankingDomainStats(String domain){
        return getSERankingDomainStats(domain, "es", 1);
    }

    public Object getSERankingDomainStats(String domain, String source, int withSubdomains){
        String endpoint = this.baseUrl + "/" + source + "/overview/";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token " + apiKey);
        headers.add("Content-Type", "application/json; charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("domain", domain);
        params.add("with_subdomains", String.valueOf(withSubdomains));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<Object> response = new RestTemplate().exchange(
                endpoint,
                HttpMethod.GET,
                entity,
                Object.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException(response.getBody().toString());
        }
    }

    public Object getSERankingDomainComparative(String domain, String competitor){
        return getSERankingDomainComparative(domain, competitor, "es", "organic", 0, 1000);
    }

    public Object getSERankingDomainComparative(String domain, String competitor, String source, String type, int diff, int limit) {
        String endpoint = this.baseUrl + "/" + source + "/competitors/compare";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token " + apiKey);
        headers.add("Content-Type", "application/json; charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("domain", domain);
        params.add("compare", competitor);
        params.add("type", type);
        params.add("diff", String.valueOf(diff));
        params.add("limit", String.valueOf(limit));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<Object> response = new RestTemplate().exchange(
                endpoint,
                HttpMethod.GET,
                entity,
                Object.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException(response.getBody().toString());
        }
    }

    public List<keyworkData> getSeRankingKeywordsMetrics(List<String> keywords, String source) throws IOException {
        String endpoint = this.baseUrl + "/" + source + "/analyze-keywords/";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Token " + apiKey);
        headers.add("Content-Type", "application/json; charset=utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData;
        try {
            jsonData = objectMapper.writeValueAsString(keywords);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize keywords to JSON.");
        }

        HttpEntity<String> entity = new HttpEntity<>(jsonData, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(
                baseUrl + endpoint,
                entity,
                Object.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return objectMapper.readValue((JsonParser) response.getBody(), new TypeReference<List<keyworkData>>() {});
        } else {
            throw new RuntimeException(response.getBody().toString());
        }
    }
}
