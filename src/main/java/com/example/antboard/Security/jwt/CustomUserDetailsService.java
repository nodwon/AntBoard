package com.example.antboard.Security.jwt;

import com.example.antboard.dto.response.member.MemberPrincipal;
import com.example.antboard.dto.response.member.MemberTokenDto;
import com.example.antboard.entity.Member;
import com.example.antboard.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Assuming we create a method in MemberTokenDto to build itself from a Member entity
        MemberTokenDto memberTokenDto = MemberTokenDto.from(member);

        return MemberPrincipal.create(member);
    }
}
