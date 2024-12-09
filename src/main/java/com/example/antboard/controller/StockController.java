//package com.example.antboard.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//@RequestMapping("/api/stock")
//public class StockController {
//
//    private final RestTemplate restTemplate;
//
//    @Value("${korea-investment.base-url}")
//    private String stockApiUrl;
//
//    @Value("${korea-investment.appkey}")
//    private String apiKey;
//    @Value("${korea-investment.appsecret}")
//    private String appSecret;
//
//    public StockController(RestTemplateBuilder restTemplateBuilder) {
//        this.restTemplate = restTemplateBuilder.build();
//    }
//
//    @GetMapping("/{symbol}")
//    public ResponseEntity<Object> getStockData(@PathVariable String symbol) {
//        String url = String.format("%s/query?function=TIME_SERIES_INTRADAY&symbol=%s&interval=5min&apikey=%s", stockApiUrl, symbol, apiKey);
//
//        try {
//            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
//            return ResponseEntity.ok(response.getBody());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching stock data: " + e.getMessage());
//        }
//    }
//}
//
