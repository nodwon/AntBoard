package com.example.antboard.controller;

import com.example.antboard.dto.response.balance.BalanceResponse;
import com.example.antboard.service.BalanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/balance")
    public BalanceResponse getBalance(@RequestParam String cano,@RequestParam String acntPrdtCd) {
        return balanceService.getBalance(cano, acntPrdtCd);
    }
}
