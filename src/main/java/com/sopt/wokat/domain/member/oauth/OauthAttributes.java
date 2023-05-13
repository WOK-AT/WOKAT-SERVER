package com.sopt.wokat.domain.member.oauth;

import java.util.Arrays;
import java.util.Map;

import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.global.config.security.provider.KakaoUserInfo;

public enum OauthAttributes {
    
    KAKAO("kakao") {
        @Override
        public Member of(Map<String, Object> attributes) {
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
            return Member.createMember(
                kakaoUserInfo.getNickName(),
                kakaoUserInfo.getProfileImage(),
                kakaoUserInfo.getUserEmail(),
                kakaoUserInfo.getProvider(),
                kakaoUserInfo.getProviderId()
            );
        }
    };

    private final String providerName;

    OauthAttributes(String providerName) {
        this.providerName = providerName;
    }

    public static Member extract(String providerName, Map<String, Object> memberAttributes) {
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of(memberAttributes);
    }

    public abstract Member of(Map<String, Object> attributes);
}
