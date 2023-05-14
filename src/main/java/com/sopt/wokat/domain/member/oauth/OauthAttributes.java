package com.sopt.wokat.domain.member.oauth;

import java.util.Arrays;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.entity.MemberProfile;
import com.sopt.wokat.domain.member.entity.Role;
import com.sopt.wokat.domain.member.repository.MemberRepository;
import com.sopt.wokat.global.config.security.provider.KakaoUserInfo;

public enum OauthAttributes {
    KAKAO("kakao") {

        @Override
        public Member of(Map<String, Object> attributes) {
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);

            MemberProfile profile = MemberProfile.createProfile(
                new ObjectId(),
                kakaoUserInfo.getNickName(),
                kakaoUserInfo.getProfileImage(),
                kakaoUserInfo.getUserEmail(),
                kakaoUserInfo.getProvider(),
                kakaoUserInfo.getProviderId()
            );

            return Member.createMember(
                profile,
                Role.ROLE_MEMBER
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
