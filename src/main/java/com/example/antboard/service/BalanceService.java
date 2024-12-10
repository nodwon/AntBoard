package com.example.antboard.service;

import com.example.antboard.dto.response.balance.BalanceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BalanceService {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String appKey;
    private final String appSecret;
    private final String apiBearerToken;
    private final String trId;


    public BalanceService(RestTemplate restTemplate,
                          @Value("${api.baseurl}") String baseUrl,
                          @Value("${api.appkey}") String appKey,
                          @Value("${api.appsecret}") String appSecret,
                          @Value("${api.apiBearerToken}") String apiBearerToken,
                          @Value("${api.trid}") String trId){
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.apiBearerToken = apiBearerToken;
        this.trId = trId;
    }

    public BalanceResponse getBalance(@Value("${api.cano}")String cano, @Value("${api.acntPrdtCd}")String acntPrdtCd) {
        // Build the URL
        String url = String.format("%s/uapi/domestic-stock/v1/trading/inquire-balance?CANO=%s&ACNT_PRDT_CD=%s&AFHR_FLPR_YN=N&OFL_YN=&INQR_DVSN=01&UNPR_DVSN=01&FUND_STTL_ICLD_YN=N&FNCG_AMT_AUTO_RDPT_YN=N&PRCS_DVSN=00",
                baseUrl.replace("ws://", "https://"), cano, acntPrdtCd);


        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiBearerToken); // 한투 API 토큰
        headers.set("appkey", appKey);
        headers.set("appsecret", appSecret);
        headers.set("tr_id", trId);
        headers.set("cano", cano);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the GET request
        ResponseEntity<BalanceResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                BalanceResponse.class
        );

        return response.getBody();
    }
}
