package com.sopt.wokat.infra.tmap.WalkingDistance;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Features {
    
    @JsonProperty("properties")
    private Properties properties;

}
