package com.sopt.wokat.domain.place.repository;


import com.sopt.wokat.domain.place.entity.SpaceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;



@Repository
@RequiredArgsConstructor
public class PlaceRepositoryImpl implements PlaceRepositoryCustom{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public SpaceInfo findByPlaceId(String placeId) {
        Query findByIdQuery = new Query(Criteria.where("_id").is(placeId));


        SpaceInfo foundPlace = mongoTemplate.findOne(findByIdQuery, SpaceInfo.class);

        return foundPlace;
    }

}
