package com.sopt.wokat.OpenAPI;


import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sopt.wokat.domain.place.dto.CoordinateDTO;
import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.error.exception.KakaoAPIRequestException;
import com.sopt.wokat.infra.tmap.WalkingDistance.APIGetWalkingDist;

@SpringBootTest
public class TmapTest {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Autowired
    private APIGetWalkingDist apiGetWalkingDist; 

    @Test
    public void getDistance() {
        CoordinateDTO departCoord = new CoordinateDTO(127.004943, 37.50481);
        CoordinateDTO destCoord = new CoordinateDTO( 126.995925, 37.503415);

        try {
            int totalTime = apiGetWalkingDist.getWalkingDistance(departCoord, "출발지", destCoord, "도착지");
            LOGGER.info(totalTime);
        } catch (URISyntaxException e) {
            throw new KakaoAPIRequestException(ErrorCode.GET_WALK_DISTANCE_FAIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
    }

}
