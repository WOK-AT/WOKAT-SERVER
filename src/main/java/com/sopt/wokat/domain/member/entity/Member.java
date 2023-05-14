package com.sopt.wokat.domain.member.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.sopt.wokat.global.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "Member")
@Schema(description = "Member 테이블")
public class Member extends BaseEntity {

    @DBRef(lazy = true)
    @Schema(description = "유저 프로필 세부정보")
    private MemberProfile memberProfile;

    @Field("role")
    @Schema(description = "유저 ROLE")
    private Role role;

    @Builder
    public Member(MemberProfile memberProfile, Role role) {
        this.memberProfile = memberProfile;
        this.role = role;
    }

    public static Member createMember(String nickName, String profileImage, String userEmail, String provider, String providerId, Role role) {
        MemberProfile profile = MemberProfile.createProfile(nickName, profileImage, userEmail, provider, providerId);

        Member member = Member.builder()
                            .memberProfile(profile)
                            .role(role)
                            .build();
        return member;
    }

}