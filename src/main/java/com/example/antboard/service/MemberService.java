package com.example.antboard.service;

import com.example.antboard.common.exception.MemberException;
import com.example.antboard.dto.request.member.JoinDto;
import com.example.antboard.dto.request.member.MemberRegisterDto;
import com.example.antboard.dto.response.member.MemberResponseDto;
import com.example.antboard.dto.response.member.MemberTokenDto;
import com.example.antboard.entity.Member;
import com.example.antboard.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final PasswordEncoder encoder;
    private final MemberRepository memberRepository;

    private final AuthenticationManager authenticationManager;


    public HttpStatus checkIdDuplicate(String email){
        isExistEmail(email);
        return HttpStatus.OK;
    }
    public MemberResponseDto register(MemberRegisterDto registerDto){
        isExistEmail(registerDto.getEmail());
        checkPassword(registerDto.getPassword(), registerDto.getPasswordCheck());

        String encodePwd = encoder.encode(registerDto.getPassword());
        registerDto.setPassword(encodePwd);
        Member saveMember = memberRepository.save(MemberRegisterDto.of(registerDto));
        return MemberResponseDto.from(saveMember);
    }

    private void checkPassword(String password, String passwordCheck) {
        if(!password.equals(passwordCheck)){
            throw new MemberException("패스워드 불일치", HttpStatus.BAD_REQUEST);
        }
    }

    private void isExistEmail(String email) {
        if(memberRepository.findByEmail(email).isPresent()){
            throw new MemberException("이미사용중인 이메일입니다.",HttpStatus.BAD_REQUEST);
        }
    }
    public MemberTokenDto login(JoinDto joinDto) {
        authenicate(joinDto.getEmail(), joinDto.getPassword());
        UserDetails userDetails =
    }
    public MemberResponseDto check(Member member, String password) {
    }

    private  void authenicate(String email, String pwd){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, pwd));
        }catch (DisabledException e){
            throw new MemberException("인증되지않는 아이디입니다.", HttpStatus.BAD_REQUEST);
        }catch (BadCredentialsException e){
            throw new MemberException("비밀번호가 일치하지 않습니다.",HttpStatus.BAD_REQUEST);
        }
    }
}
