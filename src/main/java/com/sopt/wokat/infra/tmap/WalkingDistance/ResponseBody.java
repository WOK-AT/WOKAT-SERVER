package com.sopt.wokat.infra.tmap.WalkingDistance;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ResponseBody {
    
    @JsonProperty("features")
    private Features[] features;
    
}
