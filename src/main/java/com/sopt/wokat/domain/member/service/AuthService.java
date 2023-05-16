package com.sopt.wokat.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopt.wokat.domain.member.dto.LoginMember;
import com.sopt.wokat.domain.member.dto.RefreshTokenRequest;
import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.exception.JwtInvalidException;
import com.sopt.wokat.global.config.redis.RedisUtil;
import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.util.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    private final MemberService memberService;

    //! Access Token 유효성 확인
    public void validateAccessToken(String accessToken) {
        accessTokenExtractor(accessToken);
    }

    //! Access Token으로 회원 조회 
    @Transactional(readOnly = true) 
    public LoginMember findMemberByAccessToken(String accessToken) {
        if (!accessToken.isEmpty()) {
            accessTokenExtractor(accessToken);
        }

        String id = jwtTokenProvider.getPayload(accessToken);
        Member findMember = memberService.findById(id);
        
        return new LoginMember(findMember.getId());
    }

    private void accessTokenExtractor(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new JwtInvalidException();
        }
    }

    private void refreshTokenExtractor(RefreshTokenRequest refreshTokenRequest) {
        if (!jwtTokenProvider.validateToken(refreshTokenRequest.getRefreshToken())) {
            throw new JwtInvalidException(ErrorCode.UNAUTHORIZED_REFRESH_TOKEN);
        }
    }
}
