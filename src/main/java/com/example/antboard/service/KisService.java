package com.example.antboard.service;

import com.example.antboard.Security.config.KisConfig;
import com.example.antboard.common.AccessTokenManager;
import com.example.antboard.entity.KisBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class KisService {
    private final WebClient webClient;
    private final AccessTokenManager accessTokenManager;
    private final KisConfig kisConfig;

    @Autowired
    public KisService(WebClient.Builder webClientBuilder, AccessTokenManager accessTokenManager, KisConfig kisConfig) {
        this.webClient = webClientBuilder.baseUrl(kisConfig.getRestBaseUrl()).build();
        this.accessTokenManager = accessTokenManager;
        this.kisConfig = kisConfig;
    }

    public String getStringToday(){
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return localDate.format(formatter);
    }
    public String getJobDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public Mono<String> getMajorIndex(String iscd, String fidCondMrktDivCode) {
        String path = fidCondMrktDivCode.equals("U") ? KisConfig.FHKUP03500100_PATH : KisConfig.FHKST03030100_PATH;
        String trId = fidCondMrktDivCode.equals("U") ? "FHKUP03500100" : "FHKST03030100";


        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("fid_cond_mrkt_div_code", fidCondMrktDivCode)
                        .queryParam("fid_input_iscd", iscd)
                        .queryParam("fid_input_date_1", getStringToday())
                        .queryParam("fid_input_date_2", getStringToday())
                        .queryParam("fid_period_div_code", "D")
                        .build())
                .header("content-type", "application/json")
//                .header("authorization", "Bearer " + accessTokenManager.getAccessToken())
                .header("authorization", "Bearer " + kisConfig.getApiBearerToken())
                .header("appkey", kisConfig.getAppKey())
                .header("appsecret", kisConfig.getAppSecret())
                .header("tr_id", trId)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), clientResponse -> {
                    // 에러 응답 로깅
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException("API Error: " + errorBody)));
                })
                .bodyToMono(String.class)
                .doOnError(e -> log.error("Failed to fetch major index data: {}", e.getMessage()));
    }

    public Mono<KisBody> getCurrentPrice(String id) {
        String url = kisConfig.getRestBaseUrl() + "/uapi/domestic-stock/v1/quotations/inquire-price?fid_cond_mrkt_div_code=J&fid_input_iscd=" + id;
        log.error("asdf{}", kisConfig.getApiBearerToken());
        return webClient.get()
                .uri(url)
                .header("content-type", "application/json")
                .header("authorization", "Bearer " + kisConfig.getApiBearerToken())
                .header("appkey", kisConfig.getAppKey())
                .header("appsecret", kisConfig.getAppSecret())
                .header("tr_id", kisConfig.getTrid())
                .retrieve()
                .bodyToMono(KisBody.class);
    }
    public Mono<KisBody> getboardAccount(String id) {
        String url = kisConfig.getRestBaseUrl() + "/uapi/domestic-stock/v1/quotations/inquire-price?fid_cond_mrkt_div_code=J&fid_input_iscd=" + id;

        return webClient.get()
                .uri(url)
                .header("content-type", "application/json")
                .header("authorization", "Bearer " + kisConfig.getApiBearerToken())
                .header("cano", kisConfig.getCano())
                .header("ACNT_PRDT_CD", "01")
                .header("AFHR_FLPR_YN", "N")
                .header("OFL_YN", "")
                .header("INQR_DVSN", "01")
                .header("UNPR_DVSN", "01")
                .header("FUND_STTL_ICLD_YN", "N")
                .header("FNCG_AMT_AUTO_RDPT_YN", "N")
                .header("PRCS_DVSN", "00")
                .header("CTX_AREA_FK100", "")
                .header("CTX_AREA_NK100", "")

                .retrieve()
                .bodyToMono(KisBody.class);
    }

}

