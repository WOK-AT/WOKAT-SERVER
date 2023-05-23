package com.sopt.wokat.domain.place.dto;

import java.util.List;
import java.util.Map;

import com.sopt.wokat.domain.place.entity.Space;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostPlaceRequest {
    
    private String spaceClass;
    private String area;
    private String homepageURL;
    private String name;
    private String isFree;
    private String isRequiredReserve;
    private String socket;
    private String parkingLot;
    private String hdmiScreen;
    private String locationRoadName;
    private String locationLotNumber;
    private String headCount;
    private List<String> introduction;
    private List<String> contact;
    private List<String> hashTags; 
    private Map<String, Object> wifi;
    private Map<String, Object> openTime;
    private Map<String, Object> distance;

}
