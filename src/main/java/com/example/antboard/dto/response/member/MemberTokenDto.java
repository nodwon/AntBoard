package com.example.antboard.dto.response.member;

import com.example.antboard.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberTokenDto {

    private String email;
    private String role;

    @Builder
    public MemberTokenDto(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public static MemberTokenDto from(Member member) {
        return MemberTokenDto.builder()
                .email(member.getEmail())
                .role(member.getRole().toString())
                .build();
    }


}
