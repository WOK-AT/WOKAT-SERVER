package com.sopt.wokat.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceImageUploadDTO {
    
    private String s3ImageURL;
    private String fileName;
    
}
