package com.sopt.wokat.domain.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopt.wokat.domain.member.dto.MemberResponse;
import com.sopt.wokat.domain.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "Member", description = "유저 관련 API") 
public class MemberController {
    
    private final MemberService memberService;

    @Operation(summary = "토큰 통해 유저 식별 로직", description = "헤더의 Access Token을 통해 유저를 식별하는 로직이 담긴 API입니다.", tags = {"Member"})
    @GetMapping(value = "")
    public MemberResponse getMember(){
        return memberService.getMemberFromAccessToken();
    }

}
