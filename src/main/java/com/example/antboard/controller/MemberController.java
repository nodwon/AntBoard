package com.example.antboard.controller;

import com.example.antboard.dto.request.member.JoinDto;
import com.example.antboard.dto.request.member.LoginDto;
import com.example.antboard.dto.response.member.MemberPrincipal;
import com.example.antboard.dto.response.member.MemberResponseDto;
import com.example.antboard.dto.response.member.MemberTokenDto;
import com.example.antboard.dto.response.member.MemberUpdateDto;
import com.example.antboard.entity.Member;
import com.example.antboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/all")
    public ResponseEntity<String> test(@AuthenticationPrincipal MemberPrincipal userDetails) {

        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return ResponseEntity.ok("success");

    }

    @GetMapping("/chekId")
    public ResponseEntity<?> checkIdDuplicate(@RequestParam String email) {
        memberService.checkIdDuplicate(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> register(@RequestBody JoinDto joinDto) throws Exception {
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
