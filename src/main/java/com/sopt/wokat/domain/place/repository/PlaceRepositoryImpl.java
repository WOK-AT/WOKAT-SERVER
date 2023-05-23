package com.sopt.wokat.domain.place.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.sopt.wokat.domain.place.dto.OnePlaceInfoResponse;
import com.sopt.wokat.domain.place.dto.PostPlaceRequest;
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
    public OnePlaceInfoResponse findByIdCustom(String id) {
        SpaceInfo spaceInfo = mongoTemplate.findById(id, SpaceInfo.class);
        if (spaceInfo == null) throw new PlaceNotFoundException();

        OnePlaceInfoResponse placeInfoResponse = OnePlaceInfoResponse.creatOnePlaceInfoResponse(spaceInfo);
        return placeInfoResponse;
    }

}
