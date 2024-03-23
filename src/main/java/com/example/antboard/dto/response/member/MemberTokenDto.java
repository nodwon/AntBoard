package com.example.antboard.dto.response.member;

import com.example.antboard.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
public class MemberTokenDto {

    private String email;
    private static String Role;

    @Builder
    public MemberTokenDto(String email, String Role){
        this.email = email;
        this.Role = Role;
    }

    public static MemberTokenDto from(String email){
        return MemberTokenDto.builder()
                .email(email)
                .Role(Role)
                .build();
    }
}
