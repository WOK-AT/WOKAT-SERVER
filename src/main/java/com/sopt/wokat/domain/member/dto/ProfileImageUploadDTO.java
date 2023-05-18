package com.sopt.wokat.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImageUploadDTO {
    
    private String s3ImageURL;
    private String fileName;

}
