package com.sopt.wokat.infra.kakao.LocationToCoord;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * "documents": [
            {
            "x": "126.998834230134",
            "y": "37.5826436294265"
            , ...
        }, ...
    ]
    에서 address_name 가져오기
 */
@Data
@AllArgsConstructor @NoArgsConstructor
public class APIDocument {
    
    @JsonProperty("x")
    private String longitude;

    @JsonProperty("y")
    private String latitude;

}
