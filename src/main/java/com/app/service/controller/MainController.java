package com.app.service.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MainController {

    @GetMapping("/")
    public List<ListClass> getEndpoints() {
        List<ListClass> endpoints = new ArrayList<>();

        // Leer el archivo JSON con los endpoints
        try {
            String jsonContent = new String(Files.readAllBytes(new File("./endpoints.json").toPath()));
            ObjectMapper objectMapper = new ObjectMapper();
            endpoints = objectMapper.readValue(jsonContent, new TypeReference<List<ListClass>>() {
            });
        } catch (IOException e) {
            // Manejar errores de lectura del archivo JSON
            e.printStackTrace();
        }

        // Modificar los datos de los endpoints si es necesario
        for (ListClass endpoint : endpoints) {
            endpoint.setExample(ServletUriComponentsBuilder.fromCurrentRequest()
                    .path(endpoint.getExample()).build().toUriString());
        }

        return endpoints;
    }

    @GetMapping("/domain/compare/")
    private Object api_domain_compare() {

        return null;
    }

    @GetMapping("/domain/stats/")
    private Object api_domain_overview() {

        return null;
    }

    @GetMapping("/domain/competitors/")
    private Object api_domain_competitors() {

        return null;
    }

    @GetMapping("/domain/keywords/")
    private Object api_domain_keywords() {

        return null;
    }

    @GetMapping("/domain/backlinks/")
    private Object api_domain_backlinks() {

        return null;
    }

    @GetMapping("/domain/posts/")
    private Object api_domain_posts() {

        return null;
    }

    @GetMapping("/keyword/overview/")
    private Object api_keyword_overview() {

        return null;
    }

    @GetMapping("/keyword/bulk/")
    private Object api_keyword_bulk() {

        return null;
    }
}
