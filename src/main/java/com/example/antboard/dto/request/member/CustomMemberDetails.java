//package com.example.antboard.dto.request.member;
//
//import com.example.antboard.entity.Member;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Collections;
//
//@AllArgsConstructor
//public class CustomMemberDetails implements UserDetails {
//
//    private final Member member;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singletonList(new SimpleGrantedAuthority(String.valueOf(member.getRole())));
//    }
//
//    @Override
//    public String getPassword() {
//        return "";
//    }
//
//    @Override
//    public String getUsername() {
//        return member.getEmail();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//}
