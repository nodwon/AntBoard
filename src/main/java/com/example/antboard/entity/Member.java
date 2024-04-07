package com.example.antboard.entity;

import com.example.antboard.common.BaseEntity;
import com.example.antboard.common.Role;
import com.example.antboard.dto.response.member.MemberTokenDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    // 이메일로 로그인함
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    @Setter
    @Column(nullable = false)
    private String username;
    @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Comment> comments = new ArrayList<>();

    //== 생성자 Builder ==//
    @Builder
    public Member(String email, String password, String username, Role role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }
    public static Member from(String email) {
        return Member.builder()
                .email(email)
                .build();
    }

    //== update ==//
    public void update(String password, String username) {
        this.password = password;
        this.username = username;
    }
}
