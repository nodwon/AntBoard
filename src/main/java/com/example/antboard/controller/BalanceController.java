package com.example.antboard.controller;

import com.example.antboard.service.BalanceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

//    @GetMapping("/balance")
//    public BalanceResponse getBalance() {
//        return balanceService.getBalance();
//    }
}
