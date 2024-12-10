package com.example.antboard.common;

import com.example.antboard.Security.config.KisConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AccessTokenManager {
    private final WebClient webClient;
    public static String ACCESS_TOKEN;
    public static final long last_auth_time =0;

    public AccessTokenManager(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(KisConfig.REST_BASE_URL).build();
    }
    public String getAccessToken() {
        if(ACCESS_TOKEN == null){
            ACCESS_TOKEN = generateAccessToken();
        }
        return ACCESS_TOKEN;
    }
    public String generateAccessToken() {
        String url = KisConfig.REST_BASE_URL + "/oauth/token";

    }
}
