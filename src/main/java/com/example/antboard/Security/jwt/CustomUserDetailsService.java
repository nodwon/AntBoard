package com.example.antboard.Security.jwt;

import com.example.antboard.common.ResourceNotFoundException;
import com.example.antboard.common.Role;
import com.example.antboard.dto.request.member.CustomMemberDetails;
import com.example.antboard.entity.Member;
import com.example.antboard.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        Member member =  this.memberRepository.findByEmail(username).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Member Email : ", username));
        String role = String.valueOf(Role.USER);
        return member !=null ? new CustomMemberDetails(member,role): null;
    }
}
