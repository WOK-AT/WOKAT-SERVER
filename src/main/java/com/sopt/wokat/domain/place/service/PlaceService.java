package com.sopt.wokat.domain.place.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sopt.wokat.domain.place.dto.CoordinateDTO;
import com.sopt.wokat.domain.place.dto.FilteringPlaceRequest;
import com.sopt.wokat.domain.place.dto.FilteringPlaceResponse;
import com.sopt.wokat.domain.place.dto.OnePlaceInfoResponse;
import com.sopt.wokat.domain.place.dto.PostPlaceRequest;
import com.sopt.wokat.domain.place.dto.PostPlaceResponse;
import com.sopt.wokat.domain.place.entity.Space;
import com.sopt.wokat.domain.place.entity.SpaceInfo;
import com.sopt.wokat.domain.place.entity.Station;
import com.sopt.wokat.domain.place.exception.PlaceNotFoundException;
import com.sopt.wokat.domain.place.repository.PlaceRepository;
import com.sopt.wokat.domain.place.repository.StationRepository;
import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.error.exception.KakaoAPIRequestException;
import com.sopt.wokat.infra.kakao.CoordToLocation.APICoordToLocation;

import lombok.RequiredArgsConstructor;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final StationRepository stationRepository;

    @Autowired
    private APICoordToLocation apiCoordToLocation;
    
    public List<FilteringPlaceResponse> filteringPlace(String placeClass, FilteringPlaceRequest filteringPlaceRequest) {
        String area;
        Space space = Space.fromValue(placeClass);

        ///! 역명 decode하기 
        String decodedStation = URLDecoder.decode(filteringPlaceRequest.getStation(), StandardCharsets.UTF_8);
        List<Station> stations = stationRepository.findByName(decodedStation);
        CoordinateDTO stationCoord = new CoordinateDTO(stations.get(0).getLongitude(), 
                    stations.get(0).getLatitude());

        //! 역의 위경도 통해 지역 찾기 
        try {
            area = apiCoordToLocation.getAreaByCoordinates(
                    stations.get(0).getLongitude(), stations.get(0).getLatitude());
        } catch (URISyntaxException e){
            throw new KakaoAPIRequestException(ErrorCode.COORDS_TO_LOCATION_FAIL); 
        }

        //! 역이 1~9호선에 속하는지 확인
        Boolean isMainStation = isStationHasWalkDistance(stations);

        if (area == null) return new ArrayList<>();     //! 서울특별시 아닌 경우 
        return placeRepository.findSpaceByProperties(space, area, filteringPlaceRequest.getStation(), isMainStation,
                    stationCoord, filteringPlaceRequest);
    }

    public Boolean isStationHasWalkDistance(List<Station> stations) {
        String line = stations.get(0).getLine();

        return Arrays.asList("01호선", "02호선", "03호선", "04호선", "05호선", "06호선", "07호선", "08호선", "09호선")
                    .contains(line);
    }

    public PostPlaceResponse postPlace(List<MultipartFile> multipartFile, PostPlaceRequest placeRequest) throws IOException {
        SpaceInfo space = placeRepository.savePlace(multipartFile, placeRequest);

        PostPlaceResponse placeResponse = new PostPlaceResponse(
            space.getId(),
            space.getName()
        );

        return placeResponse;
    }

    public OnePlaceInfoResponse findPlaceInfo(String placeId, String station) {
        OnePlaceInfoResponse placeInfoResponse = placeRepository.findByIdCustom(placeId, station);
        return placeInfoResponse;
    }

    public String findPlaceLocation(String placeId, int isRoadName) {
        SpaceInfo foundPlace = placeRepository.findById(placeId)
                    .orElseThrow(PlaceNotFoundException::new);
        String address = (isRoadName == 0) ? 
                    foundPlace.getLocationRoadName() : foundPlace.getLocationLotNumber();
        return address;
    }

}
