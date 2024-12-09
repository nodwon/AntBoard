package com.example.antboard.dto.response.balance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BalanceResponse {
    @JsonProperty("output1")
    private List<Holding> holdings;

    @JsonProperty("output2")
    private List<AccountSummary> accountSummary;

    @Data
    public static class Holding {
        private String pdno;
        private String prdt_name;
        private String hldg_qty;
        private String ord_psbl_qty;
        private String pchs_avg_pric;
        private String prpr;
        private String evlu_amt;
        private String evlu_pfls_amt;
    }

    @Data
    public static class AccountSummary {
        private String dnca_tot_amt;
        private String scts_evlu_amt;
        private String evlu_pfls_smtl_amt;
    }
}
