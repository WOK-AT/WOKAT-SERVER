package com.sopt.wokat.DB;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.sopt.wokat.domain.place.entity.Station;
import com.sopt.wokat.domain.place.repository.PlaceRepository;
import com.sopt.wokat.domain.place.repository.StationRepository;
import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.error.exception.KakaoAPIRequestException;
import com.sopt.wokat.infra.kakao.APICoordToLocation;

@SpringBootTest
public class StationTest {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private APICoordToLocation apiCoordToLocation;

    @Test
    public void findStations() {
        Query query = new Query(Criteria.where("name").is("고속터미널"));
        List<Station> stations = mongoTemplate.find(query, Station.class, "Station");
        LOGGER.info(stations);

        List<Station> stationList = stationRepository.findByName("고속터미널");
        LOGGER.info(stationList);
    }

    @Test
    public void convertCoordsToLocation() {
        String longitude = "127.028461";
        String latitude = "37.527072";

        try {
            String area = apiCoordToLocation.getAreaByCoordinates(longitude, latitude);
            LOGGER.info(area);
        } catch (URISyntaxException e) {
            throw new KakaoAPIRequestException(ErrorCode.COORDS_TO_LOCATION_FAIL);
        }
    }

}
