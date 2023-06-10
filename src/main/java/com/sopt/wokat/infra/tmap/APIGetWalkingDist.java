package com.sopt.wokat.infra.tmap;

import org.springframework.beans.factory.annotation.Value;

public class APIGetWalkingDist {
    
    @Value("${tmap.client-id}")
    private String TMAP_API_KEY;
}
