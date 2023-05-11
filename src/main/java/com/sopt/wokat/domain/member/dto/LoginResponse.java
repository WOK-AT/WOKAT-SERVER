package com.sopt.wokat.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {

    private Long id;
    private String nickName;
    private String profileImage;
    private String userEmail;
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    boolean ProfileSaveUser;

    @Builder
    public LoginResponse(Long id, String nickName, String profileImage, String userEmail, String tokenType, String accessToken, String refreshToken, boolean profileSaveUser) {
        this.id = id;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.userEmail = userEmail;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.ProfileSaveUser = profileSaveUser;
    }

}
