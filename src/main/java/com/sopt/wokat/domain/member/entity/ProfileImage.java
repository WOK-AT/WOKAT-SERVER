package com.sopt.wokat.domain.member.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "ProfileImage")
@Schema(description = "Profile_Image 테이블")
public class ProfileImage {
    
    @Id
    @Schema(description = "Member Profile 고유 ID")
    private ObjectId id;

    @Field("user_image_s3_url")
    @Schema(description = "유저 S3 프로필 이미지 URL")
    private String s3URL;

    @Field("user_image_oauth_url")
    @Schema(description = "유저 Oauth 프로필 이미지 URL")
    private String oauthURL;

    @Builder
    public ProfileImage(ObjectId id, String s3URL, String oauthURL) {
        this.id = id;
        this.s3URL = s3URL;
        this.oauthURL = oauthURL;
    }

    public static ProfileImage createProfileImage(ObjectId id, String s3URL, String oauthURL) {
        ProfileImage profileImage = ProfileImage.builder()
                        .id(id)
                        .s3URL(s3URL)
                        .oauthURL(oauthURL)
                        .build();
                
        return profileImage;
    }
    
}
