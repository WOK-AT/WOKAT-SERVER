package com.sopt.wokat.DB;

import com.sopt.wokat.domain.place.controller.PlaceController;
import com.sopt.wokat.domain.place.entity.SpaceInfo;
import com.sopt.wokat.domain.place.repository.PlaceRepository;
import com.sopt.wokat.domain.place.service.PlaceService;
import com.sopt.wokat.global.result.ResultResponse;
import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.management.Query;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlaceTest {

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PlaceController placeController;

    @Test
    public void convertAddressTest() {
        // 생성 후
        SpaceInfo place = SpaceInfo.builder()
                        .locationLotNumber("지번주소")
                        .locationRoadName("도로명주소")
                        .build();
        // 디비에 저장
        placeRepository.save(place);

        // 검증
        ResponseEntity<ResultResponse> response = placeController.getPlaceLocation(place.getId().toString(),0);
        Assertions.assertThat(response.getBody().getData()).isEqualTo("도로명주소");

        response = placeController.getPlaceLocation(place.getId().toString(),1);
        Assertions.assertThat(response.getBody().getData()).isEqualTo("지번주소");

        // 디비에서 삭제
        placeRepository.deleteById(place.getId().toString());
    }


}
