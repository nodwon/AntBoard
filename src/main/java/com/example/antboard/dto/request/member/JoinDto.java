package com.example.antboard.dto.request.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinDto {

    private String email;
    private String password;

    @Builder
    public JoinDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
