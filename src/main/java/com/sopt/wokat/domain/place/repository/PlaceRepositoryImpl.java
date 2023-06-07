package com.sopt.wokat.domain.place.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.sopt.wokat.domain.place.dto.OnePlaceInfoResponse;
import com.sopt.wokat.domain.place.dto.PostPlaceRequest;
import com.sopt.wokat.domain.place.entity.Space;
import com.sopt.wokat.domain.place.entity.SpaceInfo;
import com.sopt.wokat.domain.place.exception.PlaceNotFoundException;
import com.sopt.wokat.infra.aws.S3PlaceUploader;
import lombok.RequiredArgsConstructor;
@Repository
@RequiredArgsConstructor
public class PlaceRepositoryImpl implements PlaceRepositoryCustom {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private S3PlaceUploader s3PlaceUploader;
    public PlaceRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public SpaceInfo savePlace(List<MultipartFile> multipartFile, PostPlaceRequest placeRequest) throws IOException {
        SpaceInfo spaceInfo = SpaceInfo.createSpaceInfo(placeRequest);
        mongoTemplate.save(spaceInfo);
        if (multipartFile != null) {
            List<String> resultURLs = s3PlaceUploader.uploadS3ProfileImage(placeRequest.getSpaceClass().toString(), multipartFile);
            spaceInfo.setImageURLs(resultURLs);
            mongoTemplate.save(spaceInfo);
        }
        return spaceInfo;
    }
    @Override
    public OnePlaceInfoResponse findByIdCustom(String category, String id) {
        SpaceInfo spaceInfo = mongoTemplate.findById(id, SpaceInfo.class);
        if (spaceInfo == null) throw new PlaceNotFoundException();
        OnePlaceInfoResponse placeInfoResponse = OnePlaceInfoResponse.creatOnePlaceInfoResponse(category, spaceInfo);
        return placeInfoResponse;
    }

    @Override
    public List<SpaceInfo> findSpaceByProperties(String area, Space space , Map<String, Object> openTime, String headCount, Pageable page){
        final Query query = new Query().with((org.springframework.data.domain.Pageable) page);

        final List<Criteria> criteria = new ArrayList<>();
        if (area != null && area.isEmpty())
        criteria.add(Criteria.where("area").is(area));

        if (space != null && space.isEmpty())
        criteria.add(Criteria.where("space").is(space));

        if (openTime != null && openTime.isEmpty())
        criteria.add(Criteria.where("openTime").is(openTime));

        if (headCount != null && headCount.isEmpty())
        criteria.add(Criteria.where("headCount").is(headCount));


        if (!criteria.isEmpty())
        query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        return mongoTemplate.find(query, SpaceInfo.class);
    }
}