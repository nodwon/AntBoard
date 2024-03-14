package com.example.antboard.dto.response.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateDto {

    private String password;
    private String passwordCheck;
    private String username;

    @Builder
    public MemberUpdateDto(String password, String passwordCheck, String username) {
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.username = username;
    }
}
