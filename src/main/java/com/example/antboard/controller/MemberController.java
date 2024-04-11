package com.example.antboard.controller;

import com.example.antboard.Security.jwt.JwtTokenProvider;
import com.example.antboard.dto.request.member.JoinDto;
import com.example.antboard.dto.request.member.LoginDto;
import com.example.antboard.dto.response.member.JwtResponseDTO;
import com.example.antboard.dto.response.member.MemberResponseDto;
import com.example.antboard.dto.response.member.MemberTokenDto;
import com.example.antboard.dto.response.member.MemberUpdateDto;
import com.example.antboard.entity.JwtToken;
import com.example.antboard.entity.Member;
import com.example.antboard.repository.JwtTokenRepository;
import com.example.antboard.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;
    // 로그인 상태 확인 엔드포인트
    @GetMapping("/status")
    public ResponseEntity<?> checkAuthStatus(HttpServletRequest request) {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            if (jwtTokenProvider.validateToken(token)) {
                String username = jwtTokenProvider.getUsername(token);
                // 토큰 저장소에서 토큰 조회
                Optional<JwtToken> tokenOptional = jwtTokenRepository.findByAccessToken(token);
                if (tokenOptional.isPresent()) {
                    return ResponseEntity.ok().body(Map.of(
                            "status", "authenticated",
                            "username", username
                    ));
                }
            }
        }
        // JWT 토큰이 없거나 유효하지 않은 경우
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "unauthenticated"));
    }
//    @GetMapping("/status")
//    public ResponseEntity<?> checkAuthStatus(@AuthenticationPrincipal Authentication authentication) {
//        // AuthenticationPrincipal을 통해 가져온 Authentication 객체가 null이 아닌지 확인
//        log.info("test stauts");
//        if (authentication != null && authentication.isAuthenticated()) {
//            return ResponseEntity.ok().body(Map.of(
//                    "status", "authenticated",
//                    "username", authentication.getName()
//            ));
//        } else {
//            // 인증되지 않은 사용자에 대해서는 Unauthorized 응답을 보냄
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "unauthenticated"));
//        }
//    }

    @GetMapping("/chekId")
    public ResponseEntity<?> checkIdDuplicate(@RequestParam String email) {
        memberService.checkIdDuplicate(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> register(@RequestBody JoinDto joinDto) {
        MemberResponseDto success = memberService.register(joinDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(success);

    }

    @PostMapping("/login")
    public ResponseEntity<MemberTokenDto> login(@RequestBody LoginDto dto) {
        MemberTokenDto login = memberService.login(dto);
        return ResponseEntity.status(HttpStatus.OK).body(login);
    }

//    @PostMapping("/login")
//    public ResponseEntity<JwtResponseDTO> AuthenicateAndGetToken(@RequestBody LoginDto dto) {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
//        MemberTokenDto login = memberService.login(dto);
//        return ResponseEntity.status(HttpStatus.OK).body(login);
//    }

    @PostMapping("/checkPwd")
    public ResponseEntity<MemberResponseDto> check(@AuthenticationPrincipal Member member, @RequestBody Map<String, String> request) {
        String password = request.get("password");
        MemberResponseDto info = memberService.check(member, password);
        return ResponseEntity.status(HttpStatus.OK).body(info);
    }

    @PutMapping("/update")
    public ResponseEntity<MemberResponseDto> update(
            @AuthenticationPrincipal Member member,
            @RequestBody MemberUpdateDto memberUpdateDTO) {
        MemberResponseDto memberUpdate = memberService.update(member, memberUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body(memberUpdate);
    }
}
