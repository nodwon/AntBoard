package com.example.antboard.service;

import com.example.antboard.Security.jwt.CustomUserDetailsService;
import com.example.antboard.Security.jwt.JwtTokenProvider;
import com.example.antboard.common.ResourceNotFoundException;
import com.example.antboard.common.exception.MemberException;
import com.example.antboard.dto.request.member.JoinDto;
import com.example.antboard.dto.request.member.LoginDto;
import com.example.antboard.dto.response.member.MemberResponseDto;
import com.example.antboard.dto.response.member.MemberTokenDto;
import com.example.antboard.dto.response.member.MemberUpdateDto;
import com.example.antboard.entity.Member;
import com.example.antboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final PasswordEncoder encoder;
    private final MemberRepository memberRepository;

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    public void checkIdDuplicate(String email) {
        isExistEmail(email);
    }

    @Transactional
    public MemberResponseDto register(JoinDto joinDto) throws Exception {
        if (isExistEmail(joinDto.getEmail())) {
            throw new Exception("이미 등록된 이메일입니다.");
        }
        checkPassword(joinDto.getPassword(), joinDto.getPasswordCheck());

        String encodePwd = encoder.encode(joinDto.getPassword());
        joinDto.setPassword(encodePwd);
        Member saveMember = memberRepository.save(JoinDto.of(joinDto));
        return MemberResponseDto.from(saveMember);
    }

    private void checkPassword(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new MemberException("패스워드 불일치", HttpStatus.BAD_REQUEST);
        }
    }

    public boolean isExistEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public MemberTokenDto login(LoginDto loginDto) {
        authenticate(loginDto.getEmail(), loginDto.getPassword());
        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Member", "email", loginDto.getEmail()));

        return MemberTokenDto.from(member);
    }

    @Transactional
    public MemberResponseDto check(Member member, String password) {
        Member checkMember = (Member) customUserDetailsService.loadUserByUsername(member.getEmail());
        checkEncodePassword(password, checkMember.getPassword());
        return MemberResponseDto.from(checkMember);
    }

    private void authenticate(String email, String pwd) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, pwd));
        } catch (DisabledException e) {
            throw new MemberException("인증되지않는 아이디입니다.", HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            throw new MemberException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 사용자가 입력한 비번과 DB에 저장된 비번이 같은지 체크 : 인코딩 확인
     */
    private void checkEncodePassword(String rawPassword, String encodedPassword) {
        if (!encoder.matches(rawPassword, encodedPassword)) {
            throw new MemberException("패스워드 불일치", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public MemberResponseDto update(Member member, MemberUpdateDto updateDto) {
        checkPassword(updateDto.getPassword(), updateDto.getPasswordCheck());
        String encodePwd = encoder.encode(updateDto.getPassword());
        Member updateMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Member Email", member.getEmail())
        );
        updateMember.update(encodePwd, updateDto.getUsername());
        return MemberResponseDto.from(updateMember);
    }


}
