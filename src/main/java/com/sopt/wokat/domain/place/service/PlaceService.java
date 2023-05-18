package com.sopt.wokat.domain.place.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sopt.wokat.domain.place.dto.OnePlaceInfoResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {
    
    public String filteringPlace() {
        return "";
    }

    public String findPlaceInfo(String placeId) {


        return "";
    }

    public String findPlaceLocation() {
        return "";
    }

}
