package com.sopt.wokat.domain.member.entity;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

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
    public MemberProfile(ObjectId id, String nickName,String profileImage, String userEmail, String provider, String providerId) {
        this.id = id;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.userEmail = userEmail;
        this.provider = provider;
        this.providerId = providerId;
    }

    //! 생성 메소드
    public static MemberProfile createProfile(ObjectId id, String nickName, String profileImage, String userEmail, String provider, String providerId) {
        MemberProfile profile =  MemberProfile.builder()
                        .id(id)
                        .nickName(nickName)
                        .profileImage(profileImage)
                        .userEmail(userEmail)
                        .provider(provider)
                        .providerId(providerId)
                        .build();
        
        return profile;
    }

}