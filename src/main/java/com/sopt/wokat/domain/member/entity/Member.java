package com.sopt.wokat.domain.member.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.sopt.wokat.global.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "Member")
@Schema(description = "Member 테이블")
public class Member extends BaseEntity {

    @DBRef(lazy = true)
    @Schema(description = "유저 프로필 세부정보")
    private MemberProfile memberProfile;

    @DBRef(lazy = true)
    @Schema(description = "유저 프로필 이미지")
    private ProfileImage profileImage;

    @Field("role")
    @Schema(description = "유저 ROLE")
    private Role role;

    @Builder
    public Member(MemberProfile memberProfile, Role role) {
        this.memberProfile = memberProfile;
        this.role = role;
    }

    public static Member createMember(MemberProfile profile, Role role) {
        Member member = Member.builder()
                            .memberProfile(profile)
                            .role(role)
                            .build();
        return member;
    }

}