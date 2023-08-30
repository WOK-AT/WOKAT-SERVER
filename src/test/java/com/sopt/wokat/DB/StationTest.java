package com.sopt.wokat.DB;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;

import com.sopt.wokat.domain.place.entity.Station;
import com.sopt.wokat.domain.place.repository.StationRepository;

@SpringBootTest
@Transactional
public class StationTest {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StationRepository stationRepository;

    @Test
    public void findStations() {
        Query query = new Query(Criteria.where("name").is("고속터미널"));
        List<Station> stations = mongoTemplate.find(query, Station.class, "Station");
        LOGGER.info(stations);

        List<Station> stationList = stationRepository.findByName("고속터미널");
        LOGGER.info(stationList);
    }

}
