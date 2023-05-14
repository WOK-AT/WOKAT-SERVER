package com.sopt.wokat.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.exception.MemberNotFoundException;
import com.sopt.wokat.domain.member.repository.MemberRepository;
import com.sopt.wokat.global.config.redis.RedisUtil;
import com.sopt.wokat.global.error.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    public Member findById(String id) {
        return getFindById(id);
    }

    private Member getFindById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());
    }

}
