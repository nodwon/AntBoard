package com.example.antboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homeController {
    @GetMapping("/api/test")
    public String hello() {
        return "테스트입니다.";
    }
}
