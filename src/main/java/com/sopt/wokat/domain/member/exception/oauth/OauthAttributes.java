package com.sopt.wokat.domain.member.exception.oauth;

import java.util.Arrays;
import java.util.Map;

import org.bson.types.ObjectId;

import com.sopt.wokat.domain.member.dto.OauthResponse;
import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.entity.Role;
import com.sopt.wokat.global.config.security.provider.KakaoUserInfo;

public enum OauthAttributes {
    KAKAO("kakao") {

        @Override
        public OauthResponse of(Map<String, Object> attributes) {
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

            Member member = Member.createMember(
                kakaoUserInfo.getNickName(),
                kakaoUserInfo.getUserEmail(),
                kakaoUserInfo.getProvider(),
                kakaoUserInfo.getProviderId()
            );

            OauthResponse response = new OauthResponse(
                member, kakaoUserInfo.getProfileImage()
            );

            return response;
        }
    };

    private final String providerName;

    OauthAttributes(String providerName) {
        this.providerName = providerName;
    }

    public static OauthResponse extract(String providerName, Map<String, Object> memberAttributes) {
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of(memberAttributes);
    }


    public abstract OauthResponse of(Map<String, Object> attributes);
}
