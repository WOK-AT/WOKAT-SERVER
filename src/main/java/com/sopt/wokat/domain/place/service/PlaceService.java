package com.sopt.wokat.domain.place.service;

import com.sopt.wokat.domain.place.entity.Space;
import com.sopt.wokat.domain.place.entity.SpaceInfo;
import com.sopt.wokat.domain.place.entity.Station;
import com.sopt.wokat.domain.place.exception.PlaceNotFoundException;
import com.sopt.wokat.domain.place.repository.PlaceRepository;
import com.sopt.wokat.domain.place.repository.StationRepository;

import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sopt.wokat.domain.place.dto.FilteringPlaceRequest;
import com.sopt.wokat.domain.place.dto.OnePlaceInfoResponse;
import com.sopt.wokat.domain.place.dto.PostPlaceRequest;
import com.sopt.wokat.domain.place.dto.PostPlaceResponse;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final StationRepository stationRepository;
    
    public List<SpaceInfo> filteringPlace(String placeClass, FilteringPlaceRequest filteringPlaceRequest) {
        Space space = Space.fromValue(placeClass);
        List<Station> stations = stationRepository.findByName(filteringPlaceRequest.getStation());

        //! 역의 위경도 통해 지역 찾기 
        String area = getAreaByStation(stations);

        return placeRepository.findSpaceByProperties(space, filteringPlaceRequest);
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

    public String findPlaceLocation(String placeId, int isRoadName) {
        SpaceInfo foundPlace = placeRepository.findById(placeId)
                .orElseThrow(PlaceNotFoundException::new);
        //! 지번 주소일 경우
        if(isRoadName == 0) return foundPlace.getLocationRoadName();
        //! 도로명 주소일 경우
        else return foundPlace.getLocationLotNumber();
    }

}
