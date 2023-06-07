package com.sopt.wokat.domain.place.repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.sopt.wokat.domain.place.dto.OnePlaceInfoResponse;
import com.sopt.wokat.domain.place.dto.PostPlaceRequest;
import com.sopt.wokat.domain.place.entity.Space;
import com.sopt.wokat.domain.place.entity.SpaceInfo;

public interface PlaceRepositoryCustom {
    
    SpaceInfo savePlace(List<MultipartFile> multipartFile, PostPlaceRequest placeRequest) throws IOException;

    OnePlaceInfoResponse findByIdCustom(String category, String id);

    public List<SpaceInfo> findSpaceByProperties(String area, Space space , Map<String, Object> openTime, String headCount, Pageable page);
}