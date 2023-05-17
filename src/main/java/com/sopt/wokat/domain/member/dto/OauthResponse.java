package com.sopt.wokat.domain.member.dto;

import com.sopt.wokat.domain.member.entity.Member;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OauthResponse {
    
    private Member member;
    private String oauthURL;

    @Builder
    public OauthResponse(Member member, String oauthURL) {
        this.member = member;
        this.oauthURL = oauthURL;
    }

}
