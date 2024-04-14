package com.example.antboard.dto.request.member;

import com.example.antboard.common.Role;
import com.example.antboard.entity.Member;
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
    private String passwordCheck;
    private String username;

    @Builder
    public JoinDto(String email, String password, String passwordCheck, String username) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.username = username;
    }

    // Dto -> Entity
    public static Member of(JoinDto dto) {
        return Member.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .username(dto.getUsername())
                .role(Role.USER)
                .build();
    }
}
