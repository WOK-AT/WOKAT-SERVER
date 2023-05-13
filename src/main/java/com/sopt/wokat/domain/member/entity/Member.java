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
    private MemberProfile memberProfile;

    @Builder
    public Member(MemberProfile memberProfile) {
        this.memberProfile = memberProfile;
    }

    public static Member createMember(String nickName, String userEmail, String provider, String providerId) {
        MemberProfile profile = MemberProfile.createProfile(nickName, userEmail, provider, providerId);

        Member member = Member.builder()
                            .memberProfile(profile)
                            .build();
        return member;
    }

}