package com.example.antboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AntBoardApplication {
    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");  // 추가
    }
    public static void main(String[] args) {
        SpringApplication.run(AntBoardApplication.class, args);
    }

}
