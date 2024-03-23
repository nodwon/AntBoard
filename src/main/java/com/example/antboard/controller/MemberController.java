package com.example.antboard.controller;

import com.example.antboard.dto.request.member.JoinDto;
import com.example.antboard.dto.response.member.MemberUpdateDto;
import com.example.antboard.dto.response.member.MemberResponseDto;
import com.example.antboard.dto.response.member.MemberTokenDto;
import com.example.antboard.entity.Member;
import com.example.antboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/chekId")
    public ResponseEntity<?> checkIdDuplicate(@RequestParam String email){
        memberService.checkIdDuplicate(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> register(@RequestBody JoinDto joinDto){
        MemberResponseDto success = memberService.register(joinDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(success);

    }

    @PostMapping("/login")
    public ResponseEntity<MemberTokenDto> login(@RequestBody JoinDto joinDto) {
        MemberTokenDto loginDTO = memberService.login(joinDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginDTO);
    }

    @PostMapping("/checkPwd")
    public ResponseEntity<MemberResponseDto> check(@AuthenticationPrincipal Member member, @RequestBody Map<String, String> request){
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
