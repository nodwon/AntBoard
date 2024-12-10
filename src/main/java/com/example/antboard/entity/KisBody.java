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
}
