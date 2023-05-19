package com.sopt.wokat.domain.place.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sopt.wokat.domain.place.dto.PostPlaceRequest;
import com.sopt.wokat.domain.place.entity.SpaceInfo;

public interface PlaceRepositoryCustom {
    
    SpaceInfo savePlace(List<MultipartFile> multipartFile, PostPlaceRequest placeRequest) throws IOException;

}
