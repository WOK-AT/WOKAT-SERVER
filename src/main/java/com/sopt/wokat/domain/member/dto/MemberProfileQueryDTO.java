package com.sopt.wokat.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor 
@AllArgsConstructor
public class MemberProfileQueryDTO {
    
    private String profileImage;
    private String nickName;
    private String userEmail;

}
