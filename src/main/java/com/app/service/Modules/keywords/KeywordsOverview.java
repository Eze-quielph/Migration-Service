package com.app.service.Modules.keywords;

import com.app.service.Modules.SeRanking.SERankingClient;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KeywordsOverview {
    @Value("${proxies")
    private String proxies;

    @Value("${seranking.api.key}")
    private String apiKey;

    private final SERankingClient seRankingClient;

    public KeywordsOverview(SERankingClient seRankingClient) {
        this.seRankingClient = seRankingClient;
    }

    public Object getQuestionsSuggets(String Keyword){
        return this.getQuestionsSuggets(Keyword, "es");
    }

    public Object getQuestionsSuggets(String keyword, String country){
        List<String> returnKws = new ArrayList<>();
        String queryCountry = "es".equals(country) ? "es_ES" : country;

        String url = "http://suggestqueries.google.com/complete/search?output=toolbar&gl=" + queryCountry + "&q=" + keyword;

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = new RestTemplate().exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            // Analizar la respuesta JSON/XML según el formato retornado por Google Suggest
            // La lógica para extraer las sugerencias de palabras clave y agregarlas a returnKws
            try{
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder  builder = factory.newDocumentBuilder();
                Document doc = builder.parse(responseBody);
                Element root = doc.getDocumentElement();

                // Aquí se extraen las sugerencias de palabras clave del XML
                NodeList suggestionList = root.getElementsByTagName("suggestion");
                for (int i = 0; i < suggestionList.getLength(); i++) {
                    Node suggestionNode = suggestionList.item(i);
                    if (suggestionNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element suggestionElement = (Element) suggestionNode;
                        String keywordData = suggestionElement.getAttribute("data");
                        if (!returnKws.contains(keywordData) && !keywordData.equals(keyword)) {
                            returnKws.add(keywordData);
                        }
                    }
                }
            }catch (ParserConfigurationException | SAXException | IOException e){
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("Error al obtener las sugerencias de palabras clave: " + response.getBody());
        }
        return returnKws;
    }
}
