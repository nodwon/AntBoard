package com.example.antboard.Security.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class KisConfig {

    public static final String REST_BASE_URL = "https://openapi.koreainvestment.com:9443";
    public static final String WS_BASE_URL = "ws://ops.koreainvestment.com:21000";
    public static final String APPKEY = "XXXXXXXXXXXXXXXXXXXXX";       // your APPKEY
    public static final String APPSECRET = "XXXXXXXXXXXXXXXXXXXXXXX";  // your APPSECRET

    public static final String FHKUP03500100_PATH = "/uapi/domestic-stock/v1/quotations/inquire-daily-indexchartprice";
    public static final String FHKST03030100_PATH = "/uapi/overseas-price/v1/quotations/inquire-daily-chartprice";
    public static final String oversea_PATH = "/uapi/domestic-stock/v1/trading/inquire-balance";
}
