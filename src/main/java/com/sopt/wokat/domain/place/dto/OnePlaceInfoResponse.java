package com.sopt.wokat.domain.place.dto;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Hidden
public class OnePlaceInfoResponse {

    private Integer category;
    private String placeName;
    private String distance;
    private String count;
    private List<String> hashtags;
    private String introduce;
    
}
