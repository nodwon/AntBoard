package com.example.antboard.Security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class KisConfig {

    public static final String FHKUP03500100_PATH = "/uapi/domestic-stock/v1/quotations/inquire-daily-indexchartprice";
    public static final String FHKST03030100_PATH = "/uapi/overseas-price/v1/quotations/inquire-daily-chartprice";

    @Value("${kis.rest-base-url}")
    public String restBaseUrl;

    @Value("${kis.appkey}")
    public String appKey;

    @Value("${kis.appsecret}")
    public String appSecret;

    @Value("${kis.trid}")
    public String trid;

    @Value("${kis.auth-token-url}")
    public String authTokenUrl;

    @Value("${kis.cano}")
    public String cano;
    @Value("${kis.apiBearerToken}")
    public String apiBearerToken;




}
