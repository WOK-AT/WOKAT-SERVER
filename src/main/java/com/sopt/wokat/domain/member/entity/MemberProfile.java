package com.sopt.wokat.domain.member.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.lang.Nullable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "MemberProfile")
@Schema(description = "Member_Profile 테이블")
public class MemberProfile {

    @Id
    @Schema(description = "Member Profile 고유 ID")
    private ObjectId id;
    
    @Field("user_name")
    @Schema(description = "유저 닉네임")
    private String nickName;

    @Email @Nullable
    @Field("user_email")
    @Schema(description = "유저 이메일")
    private String userEmail;

    @Schema(description = "간편로그인")
    private String provider;
    private String providerId;

    @Builder
    public MemberProfile(ObjectId id, String nickName, String userEmail, String provider, String providerId) {
        this.id = id;
        this.nickName = nickName;
        this.userEmail = userEmail;
        this.provider = provider;
        this.providerId = providerId;
    }

    //! 생성 메소드
    public static MemberProfile createProfile(ObjectId id, String nickName, String userEmail, String provider, String providerId) {
        MemberProfile profile =  MemberProfile.builder()
                        .id(id)
                        .nickName(nickName)
                        .userEmail(userEmail)
                        .provider(provider)
                        .providerId(providerId)
                        .build();
        
        return profile;
    }

}