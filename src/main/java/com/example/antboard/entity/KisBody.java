package com.example.antboard.entity;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "KIS")
@Getter
@Setter
@NoArgsConstructor
public class KisBody {
    private String rt_cd;
    private String msg_cd;
    private String msg1;
    private Object output;
    private String cano;
    private String ACNT_PRDT_CD;
    private String AFHR_FLPR_YN;
    private String OFL_YN;
    private String INQR_DVSN;
    private String UNPR_DVSN;
    private String FUND_STTL_ICLD_YN;
    private String FNCG_AMT_AUTO_RDPT_YN;
    private String PRCS_DVSN;
    private String CTX_AREA_FK100;
    private String CTX_AREA_NK100;
}
