package com.sopt.wokat.domain.member.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.sopt.wokat.global.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Document(collection = "Member")
@Schema(description = "Member 테이블")
public class Member extends BaseEntity {

    @Id
    @Schema(description = "Member ID")
    private String id;

    @Field("user_profile_info")
    @Schema(description = "유저 프로필 세부정보")
    private MemberProfile memberProfile;

    @Field("user_profile_image")
    @Schema(description = "유저 프로필 이미지")
    private ProfileImage profileImage;

    @Field("role")
    @Schema(description = "유저 ROLE")
    private Role role;

    @Data
    @Builder
    public static class MemberProfile {
        private String nickName;
        private String userEmail;
        private String provider;
        private String providerId;
    }

    @Data
    @Builder
    public static class ProfileImage {
        private String oauthURL;
        private String s3URL;
    }

    public static Member createMember(String nickName, String userEmail, String provider, String providerId) {
        Member member = Member.builder()
            .role(Role.ROLE_MEMBER)
            .memberProfile(Member.MemberProfile.builder()
                .nickName(nickName)
                .userEmail(userEmail)
                .provider(provider)
                .providerId(providerId)
                .build()
            )
            .build();
        return member;
    }

    public void setProfileImage(String oauthURL, String s3URL) {
        ProfileImage profileImage = ProfileImage.builder()
            .oauthURL(oauthURL)
            .s3URL(s3URL)
            .build();
        
        this.profileImage = profileImage;
    }

}