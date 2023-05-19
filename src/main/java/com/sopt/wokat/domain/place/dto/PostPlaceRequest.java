package com.sopt.wokat.domain.place.dto;

import java.util.List;

import com.sopt.wokat.domain.place.entity.Space;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostPlaceRequest {
    
    private Space spaceClass;
    private String area;
    private String homepageURL;
    private String name;
    private Boolean isFree;
    private Boolean isRequiredReserve;
    private List<String> introduction;
  //  private  


}
