package com.sopt.wokat.domain.place.dto;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Hidden
public class FilteringPlaceResponse {
    
    private String place;
    private Map<String, Object> distance;
    private String count;
    private List<String> hashtags;
    private String location;
    private String imageURL;

}
