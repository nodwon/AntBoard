package com.example.antboard.common;

import com.example.antboard.Security.config.KisConfig;
import com.example.antboard.entity.OauthInfo;
import com.example.antboard.entity.TokenInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AccessTokenManager {
    private final WebClient webClient;
    private final KisConfig kisConfig;

    private final AtomicReference<String> accessToken = new AtomicReference<>();
    private final AtomicReference<Instant> lastAuthTime = new AtomicReference<>(Instant.EPOCH);
    public AccessTokenManager(WebClient.Builder webClientBuilder, KisConfig kisConfig) {
        this.kisConfig = kisConfig;
        this.webClient = webClientBuilder.baseUrl(kisConfig.getRestBaseUrl()).build();
    }
    public synchronized String getAccessToken() {
        if (accessToken.get() == null || isTokenExpired()) {
            accessToken.set(generateAccessToken());
            lastAuthTime.set(Instant.now());
        }
        return accessToken.get();
    }

    private boolean isTokenExpired() {
        return Instant.now().isAfter(lastAuthTime.get().plusSeconds(3600)); // Assuming 1-hour token validity.
    }

    private String generateAccessToken() {
        OauthInfo bodyOauthInfo = new OauthInfo();
        bodyOauthInfo.setGrant_type("client_credentials");
        bodyOauthInfo.setAppkey(kisConfig.getAppKey());
        bodyOauthInfo.setAppsecret(kisConfig.getAppSecret());

        TokenInfo tokenInfo = webClient.post()
                .uri(kisConfig.getAuthTokenUrl())
                .header("Content-Type", "application/json")
                .bodyValue(bodyOauthInfo)
                .retrieve()
                .bodyToMono(TokenInfo.class)
                .block();

        if (tokenInfo == null || tokenInfo.getAccess_token() == null) {
            throw new RuntimeException("Unable to fetch access token.");
        }

        return tokenInfo.getAccess_token();
    }
}
