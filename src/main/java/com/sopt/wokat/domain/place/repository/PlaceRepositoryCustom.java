package com.sopt.wokat.domain.place.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sopt.wokat.domain.place.dto.FilteringPlaceRequest;
import com.sopt.wokat.domain.place.dto.OnePlaceInfoResponse;
import com.sopt.wokat.domain.place.dto.PostPlaceRequest;
import com.sopt.wokat.domain.place.entity.Space;
import com.sopt.wokat.domain.place.entity.SpaceInfo;
import com.sopt.wokat.domain.place.exception.PlaceNotFoundException;

public interface PlaceRepositoryCustom {
    
    SpaceInfo savePlace(List<MultipartFile> multipartFile, PostPlaceRequest placeRequest) throws IOException;

    OnePlaceInfoResponse findByIdCustom(String id) throws PlaceNotFoundException ;
    
    List<SpaceInfo> findSpaceByProperties(Space space, String area, FilteringPlaceRequest filteringPlaceRequest);

}
