package com.sopt.wokat.domain.place.service;

import com.sopt.wokat.domain.place.entity.SpaceInfo;
import com.sopt.wokat.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    
    public String filteringPlace() {
        return "";
    }

    public String findPlaceInfo() {
        return "";
    }

    public String findPlaceLocation(String placeId, int isRoadName) {
        SpaceInfo foundPlace = placeRepository.findByPlaceId(placeId);

        // 지번 주소일 경우
        if(isRoadName == 0){
            // 도로명 주소 반환
            return foundPlace.getLocationRoadName();
        }
        // 도로명 주소일 경우
        if(isRoadName == 1){
            // 지번 주소 반환
            return foundPlace.getLocationLotNumber();
        }

        return "";
    }

}
