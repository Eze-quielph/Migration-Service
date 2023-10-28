package com.app.service.Modules.keywords;

import com.app.service.Modules.SeRanking.SERankingClient;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

public class KeywordsBulk {
    private final SERankingClient seRankingClient;

    @Value("${seranking.api.key}")
    private String apiKey;

    public KeywordsBulk(SERankingClient seRankingClient) {
        this.seRankingClient = seRankingClient;
    }

    public Object getBulkMetrics(String db, List<String> keywords) throws IOException {
        return seRankingClient.getSeRankingKeywordsMetrics(keywords, db);
    }
}
