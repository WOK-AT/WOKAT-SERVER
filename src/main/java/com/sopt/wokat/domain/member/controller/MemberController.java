package com.sopt.wokat.domain.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopt.wokat.domain.member.dto.MemberResponse;
import com.sopt.wokat.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;

    @GetMapping
    public MemberResponse getMember(){
        return memberService.getMemberFromAccessToken();
    }

}
