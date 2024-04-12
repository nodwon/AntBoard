package com.example.antboard.dto.response.member;

import com.example.antboard.common.Role;
import com.example.antboard.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPrincipal implements UserDetails {
    private Member member;
    private Collection<GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public MemberPrincipal(Member member, Collection<GrantedAuthority> authorities){
        this.member = member;
        this.authorities =authorities;
    }
    public static MemberPrincipal create(Member member){
        return new MemberPrincipal(member, Collections.singletonList(new SimpleGrantedAuthority(Role.USER.toString())));
    }
    public static MemberPrincipal create(Member member, Map<String, Object> attributes){
        MemberPrincipal memberPrincipal =create(member);
        memberPrincipal.setAttributes(attributes);
        return memberPrincipal;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().toString());
        authorities.add(grantedAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
