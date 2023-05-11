package com.sopt.wokat.domain.member.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.lang.Nullable;
import com.sopt.wokat.global.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "MemberProfile")
@Schema(description = "Member_Profile 테이블")
public class MemberProfile {
    
    @Field("user_name")
    @Schema(description = "유저 닉네임")
    private String nickName;

    @Field("user_profileImage")
    @Schema(description = "유저 프로필 이미지 URL")
    private String profileImage;

    @Email @Nullable
    @Field("user_email")
    @Schema(description = "유저 이메일")
    private String userEmail;

    @Schema(description = "간편로그인")
    private String provider;
    private String providerId;

    @Builder
    public MemberProfile(String nickName, String profileImage, String userEmail, String provider, String providerId) {
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.userEmail = userEmail;
        this.provider = provider;
        this.providerId = providerId;
    }

    //! 생성 메소드
    public static MemberProfile createProfile(String nickName, String profileImage, String userEmail, String provider, String providerId) {
        return MemberProfile.builder()
                .nickName(nickName)
                .profileImage(profileImage)
                .userEmail(userEmail)
                .provider(provider)
                .providerId(providerId)
                .build();
    }

}