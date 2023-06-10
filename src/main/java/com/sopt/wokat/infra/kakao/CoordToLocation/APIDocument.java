package com.sopt.wokat.infra.kakao.CoordToLocation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * "documents": [
            {
            "address_name": "서울특별시 강남구 신사동",
            ...
            }, ...
    ]
    에서 address_name 가져오기
 */
@Data
@AllArgsConstructor @NoArgsConstructor
public class APIDocument {

    @JsonProperty("address_name")
    private String addressName;

}