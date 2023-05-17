package com.sopt.wokat.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopt.wokat.domain.member.dto.MemberResponse;
import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.exception.MemberNotFoundException;
import com.sopt.wokat.domain.member.repository.MemberRepository;
import com.sopt.wokat.global.config.redis.RedisUtil;
import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.util.Util;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final Util util;

    //! 헤더의 Access 토큰 통해 유저 인식
    public Member findCurrentMemberId() {
        return util.findCurrentMember();
    }

    public MemberResponse getMemberFromAccessToken() {
        Member findMember = findCurrentMemberId();
        
        return MemberResponse.builder()
                .id(findMember.getId())
                .nickName(findMember.getMemberProfile().getNickName())
                .profileImage(findMember.getProfileImage().getS3URL())
                .userEmail(findMember.getMemberProfile().getUserEmail())
                .build();
    }

    public Member findById(String id) {
        return getFindById(id);
    }

    private Member getFindById(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException());
    }

}
