package com.example.antboard.controller;

import com.example.antboard.dto.request.member.JoinDto;
import com.example.antboard.dto.request.member.LoginDto;
import com.example.antboard.dto.response.member.MemberResponseDto;
import com.example.antboard.dto.response.member.MemberTokenDto;
import com.example.antboard.dto.response.member.MemberUpdateDto;
import com.example.antboard.entity.Member;
import com.example.antboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인 상태 확인 엔드포인트
    @GetMapping("/status")
    public ResponseEntity<?> checkAuthStatus(@AuthenticationPrincipal Authentication authentication) {
        // AuthenticationPrincipal을 통해 가져온 Authentication 객체가 null이 아닌지 확인
        if (authentication != null && authentication.isAuthenticated()) {
            // 인증 객체에서 필요한 정보를 추출하여 반환
            // 여기서는 예시로 간단히 인증 상태와 사용자 이름을 반환하고 있음
            return ResponseEntity.ok().body(Map.of(
                    "status", "authenticated",
                    "username", authentication.getName()
            ));
        } else {
            // 인증되지 않은 사용자에 대해서는 Unauthorized 응답을 보냄
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "unauthenticated"));
        }
    }

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
