package com.app.service.Modules.SeRanking.Class;

import com.app.service.Modules.SeRanking.Class.KeywordInfo;

import java.util.List;

public class MyResponseClass {
    private List<KeywordInfo> keywords;

    public List<KeywordInfo> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KeywordInfo> keywords) {
        this.keywords = keywords;
    }
}

