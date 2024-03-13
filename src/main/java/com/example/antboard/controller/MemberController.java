package com.example.antboard.controller;

import com.example.antboard.dto.request.member.MemberRegisterDto;
import com.example.antboard.dto.response.member.MemberResponseDto;
import com.example.antboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    public ResponseEntity<MemberResponseDto> register(@RequestBody MemberRegisterDto memberRegisterDto){
        MemberResponseDto sucess = memberService.register(memberRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sucess);

    }
}
