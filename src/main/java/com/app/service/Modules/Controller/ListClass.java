package com.app.service.controller;
import jakarta.websocket.Endpoint;

import java.util.List;

public class ListClass {
    private  String endpoint;
    private List<String> methods;
    private  String url;
    private String example;

    public void Endpoint(String endpoint, List<String> methods, String url, String example) {
        this.endpoint = endpoint;
        this.methods = methods;
        this.url = url;
        this.example = example;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
