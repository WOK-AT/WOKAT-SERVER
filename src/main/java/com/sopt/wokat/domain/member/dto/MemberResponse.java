package com.sopt.wokat.domain.member.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Hidden
public class MemberResponse {
    
    private String id;
    private String nickName;
    private String profileImage;
    private String userEmail;
    
    @Builder
    public MemberResponse(String id, String nickName, String profileImage, String userEmail) {
        this.id = id;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.userEmail = userEmail;
    }
    
}