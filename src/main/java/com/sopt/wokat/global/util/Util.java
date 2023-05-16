package com.sopt.wokat.global.util;

import org.springframework.context.annotation.Configuration;

import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.exception.MemberNotFoundException;
import com.sopt.wokat.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class Util {
    
    private final MemberRepository memberRepository;

    public Member findCurrentMember() {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new MemberNotFoundException());
    }
    
}
