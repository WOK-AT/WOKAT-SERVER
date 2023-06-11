package com.sopt.wokat.domain.place.repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.sopt.wokat.domain.place.dto.CoordinateDTO;
import com.sopt.wokat.domain.place.dto.FilteringPlaceRequest;
import com.sopt.wokat.domain.place.dto.FilteringPlaceResponse;
import com.sopt.wokat.domain.place.dto.OnePlaceInfoResponse;
import com.sopt.wokat.domain.place.dto.PostPlaceRequest;
import com.sopt.wokat.domain.place.entity.Space;
import com.sopt.wokat.domain.place.entity.SpaceInfo;
import com.sopt.wokat.domain.place.exception.PlaceNotFoundException;
import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.error.exception.KakaoAPIRequestException;
import com.sopt.wokat.global.error.exception.TmapAPIRequestException;
import com.sopt.wokat.infra.aws.S3PlaceUploader;
import com.sopt.wokat.infra.kakao.LocationToCoord.APILocationToCoord;
import com.sopt.wokat.infra.tmap.WalkingDistance.APIGetWalkingDist;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PlaceRepositoryImpl implements PlaceRepositoryCustom {
    
    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private S3PlaceUploader s3PlaceUploader;

    @Autowired
    private APILocationToCoord apiLocationToCoord;

    @Autowired
    private APIGetWalkingDist apiGetWalkingDist;

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
    public OnePlaceInfoResponse findByIdCustom(String id) throws PlaceNotFoundException {
        SpaceInfo spaceInfo = mongoTemplate.findById(id, SpaceInfo.class);
        if (spaceInfo == null) throw new PlaceNotFoundException();

        OnePlaceInfoResponse placeInfoResponse = OnePlaceInfoResponse.createOnePlaceInfoResponse(spaceInfo);
        return placeInfoResponse;
    }

    @Override
    public List<FilteringPlaceResponse> findSpaceByProperties(Space space, String area, String station,
                    CoordinateDTO stationCoord, FilteringPlaceRequest filteringPlaceRequest) {
        //* DB에서 필터링 결과 가져오기 
        Pageable pageable = PageRequest.of(filteringPlaceRequest.getPage(), 100, 
                    Sort.by(Sort.Direction.ASC, "name"));
        Query query = new Query().with(pageable);

        List<Criteria> criteria = new ArrayList<>();
        
        //! 1) 카페/무료회의룸/무료공간 필터링
        criteria.add(Criteria.where("space").is(space));
        //! 2) 지역 필터링
        criteria.add(Criteria.where("area").is(area));
        
        query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        List<SpaceInfo> spaceList = mongoTemplate.find(query, SpaceInfo.class); 

        //! 거리순 정렬하기 
        List<SpaceInfo> sortedSpace = sortSpaceByDist(spaceList, station, stationCoord);

        //! DTO 넣기
        List<FilteringPlaceResponse> spaceReturnList = new ArrayList<>();
        for (SpaceInfo spaceInfo : sortedSpace) {
            FilteringPlaceResponse placeReturnDTO = new FilteringPlaceResponse();

            placeReturnDTO.setId(spaceInfo.getId());
            placeReturnDTO.setPlace(spaceInfo.getName());
            placeReturnDTO.setDistance(spaceInfo.getDistance());
            placeReturnDTO.setCount(spaceInfo.getHeadCount());
            placeReturnDTO.setHashtags(spaceInfo.getHashTags());
            placeReturnDTO.setLocation(spaceInfo.getLocationRoadName());
            placeReturnDTO.setImageURL(spaceInfo.getImageURLs().get(0));

            spaceReturnList.add(placeReturnDTO);
        }

        return spaceReturnList;
    }

    public List<SpaceInfo> sortSpaceByDist(List<SpaceInfo> spaceList, String station, CoordinateDTO stationCoord) {
        Collections.sort(spaceList, new Comparator<SpaceInfo>() {
            @Override
            public int compare(SpaceInfo space1, SpaceInfo space2) {
                //! 각 공간의 위경도 가져오기
                CoordinateDTO coordinate1;
                CoordinateDTO coordinate2;

                try {
                    coordinate1 = apiLocationToCoord.getCoordByLocation(space1.getLocationRoadName());
                    coordinate2 = apiLocationToCoord.getCoordByLocation(space2.getLocationRoadName());
                } catch (URISyntaxException e) {
                    throw new KakaoAPIRequestException(ErrorCode.LOCATION_TO_COORDS_FAIL);
                }
                
                //! 공간과 역의 도보거리 
                int walkTime1;
                int walkTime2;

                try {
                    walkTime1 = apiGetWalkingDist.getWalkingDistance(stationCoord, station, coordinate1, space1.getName());
                    walkTime2 = apiGetWalkingDist.getWalkingDistance(stationCoord, station, coordinate2, space1.getName());
                } catch (URISyntaxException | JSONException e) {
                    throw new TmapAPIRequestException(ErrorCode.GET_WALK_DISTANCE_FAIL);
                }
                
                int compareResult = Integer.compare(walkTime1, walkTime2);
            
                return compareResult;
            }
        });

        return spaceList;
    }

}
