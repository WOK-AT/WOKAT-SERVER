package com.sopt.wokat.OpenAPI;


import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sopt.wokat.domain.place.dto.CoordinateDTO;
import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.error.exception.KakaoAPIRequestException;
import com.sopt.wokat.infra.kakao.CoordToLocation.APICoordToLocation;
import com.sopt.wokat.infra.kakao.LocationToCoord.APILocationToCoord;

@SpringBootTest
public class KakaoTest {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Autowired
    private APICoordToLocation apiCoordToLocation;

    @Autowired
    private APILocationToCoord apiLocationToCoord;
    
    @Test
    public void convertCoordsToLocation() {
        double longitude = 127.028461;
        double latitude = 37.527072;

        try {
            String area = apiCoordToLocation.getAreaByCoordinates(longitude, latitude);
            LOGGER.info(area);
        } catch (URISyntaxException e) {
            throw new KakaoAPIRequestException(ErrorCode.COORDS_TO_LOCATION_FAIL);
        }
    }

    @Test
    public void convertLocationToCoords() {
        String location = "서울 종로구 창경궁로 236 이화빌딩 6층";

        try {
            CoordinateDTO coordinateDTO = apiLocationToCoord.getCoordByLocation(location);
            LOGGER.info(coordinateDTO);
        } catch (URISyntaxException e) {
            throw new KakaoAPIRequestException(ErrorCode.LOCATION_TO_COORDS_FAIL);
        }
    }

}
