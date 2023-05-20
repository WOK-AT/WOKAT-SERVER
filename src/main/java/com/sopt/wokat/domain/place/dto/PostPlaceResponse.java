package com.sopt.wokat.domain.place.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Hidden
public class PostPlaceResponse {
    
    private String objectId;
    private String placeName;

    @Builder
    public PostPlaceResponse(String objectId, String placeName) {
        this.objectId = objectId;
        this.placeName = placeName;
    }

}
