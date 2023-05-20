package com.sopt.wokat.domain.place.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sopt.wokat.domain.place.dto.OnePlaceInfoResponse;
import com.sopt.wokat.domain.place.dto.PostPlaceRequest;
import com.sopt.wokat.domain.place.dto.PostPlaceResponse;
import com.sopt.wokat.domain.place.entity.Space;
import com.sopt.wokat.domain.place.entity.SpaceInfo;
import com.sopt.wokat.domain.place.repository.PlaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    
    public String filteringPlace() {
        return "";
    }

    public PostPlaceResponse postPlace(List<MultipartFile> multipartFile, PostPlaceRequest placeRequest) throws IOException {
        SpaceInfo space = placeRepository.savePlace(multipartFile, placeRequest);

        PostPlaceResponse placeResponse = new PostPlaceResponse(
            space.getId(),
            space.getName()
        );

        return placeResponse;
    }

    public OnePlaceInfoResponse findPlaceInfo(String placeId) {
        OnePlaceInfoResponse placeInfoResponse = placeRepository.findByIdCustom(placeId);
        return placeInfoResponse;
    }

    public String findPlaceLocation() {
        return "";
    }

}
