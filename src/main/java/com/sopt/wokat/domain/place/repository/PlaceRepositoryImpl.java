package com.sopt.wokat.domain.place.repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
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
            List<String> resultURLs = s3PlaceUploader
                            .uploadS3ProfileImage(placeRequest.getSpaceClass().toString(), multipartFile);
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
                Boolean isMainStation, CoordinateDTO stationCoord, FilteringPlaceRequest filteringPlaceRequest) {
        List<AggregationOperation> pipeline = new ArrayList<>();

        //! 1) 카페/무료회의룸/무료공간 필터링
        pipeline.add(Aggregation.match(Criteria.where("space").is(space)));

        //! 2) 지역 필터링
        pipeline.add(Aggregation.match(Criteria.where("area").is(area)));

        //! 3-1) station이 01~09호선에 포함되는 경우 도보거리 바로 가져와서 정렬
        if (isMainStation) {
            pipeline.add(Aggregation.project()
                .and("station_distance."+station).as("walkTimeValue")  //? 도보거리 정렬 
                .andInclude("_id", "space_name", "space_distance", 
                    "space_headCount", "space_hashTag", "space_roadName", "space_images")
            );
            pipeline.add(Aggregation.sort(Sort.Direction.ASC, "walkTimeValue"));

        //! 3-2) 그 외의 노선인 경우 도보거리 api 요청 후 정렬
        } else {    
            pipeline.add(Aggregation.project()
                .andInclude("_id", "space_name", "space_distance", "space_headCount", 
                    "space_hashTag", "space_roadName", "space_images")
            );
            pipeline.add(Aggregation.sort(Sort.Direction.ASC, "space_name"));
        }

        //! Pagination 설정
        int page = filteringPlaceRequest.getPage();
        int size = 100;
        pipeline.add(Aggregation.skip((long) (page * size)));
        pipeline.add(Aggregation.limit(size));


        TypedAggregation<SpaceInfo> aggregation = Aggregation.newAggregation(SpaceInfo.class, pipeline);
        AggregationResults<SpaceInfo> results = mongoTemplate.aggregate(aggregation, SpaceInfo.class);
        List<SpaceInfo> spaceList = results.getMappedResults();
        
        //! 1~9 이외의 노선인 경우 따로 API 호출해서 정렬 
        if (!isMainStation) {
            List<SpaceInfo> sortedSpace = sortSpaceByDist(spaceList, station, stationCoord);
            spaceList = sortedSpace;
        }

        //! DTO 넣기
        List<FilteringPlaceResponse> spaceReturnList = new ArrayList<>();
        for (SpaceInfo spaceInfo : spaceList) {
            FilteringPlaceResponse placeReturnDTO = new FilteringPlaceResponse();
            String walkingDist = getWalkingDist(station, spaceInfo.getDistance());
            LOGGER.info(walkingDist);

            placeReturnDTO.setId(spaceInfo.getId());
            placeReturnDTO.setPlace(spaceInfo.getName());
            placeReturnDTO.setDistance(walkingDist);
            placeReturnDTO.setCount(spaceInfo.getHeadCount());
            placeReturnDTO.setHashtags(spaceInfo.getHashTags());
            placeReturnDTO.setLocation(spaceInfo.getLocationRoadName());
            placeReturnDTO.setImageURL(spaceInfo.getImageURLs().get(0));

            spaceReturnList.add(placeReturnDTO);
        }

        return spaceReturnList;
    }

    //* Tmap API 활용해 도보거리 측정 
    private List<SpaceInfo> sortSpaceByDist(List<SpaceInfo> spaceList, String station, CoordinateDTO stationCoord) {
        return spaceList.stream()
                .sorted(Comparator.comparingInt(space -> {
                    try {
                        CoordinateDTO coordinate = apiLocationToCoord
                                .getCoordByLocation(space.getLocationRoadName());
                        int walkTime = apiGetWalkingDist
                                .getWalkingDistance(stationCoord, station, coordinate, space.getName());
                        return walkTime;
                    } catch (URISyntaxException | JSONException e) {
                        throw new TmapAPIRequestException(ErrorCode.GET_WALK_DISTANCE_FAIL);
                    }
                }))
                .collect(Collectors.toList());
    }

    //* 검색한 역에 해당하는 도보거리 반환 or 최소 도보거리 반환
    private String getWalkingDist(String station, Map<String, Object> distances) {
        String result;
        String fullStation = station + "역";

        String stationName = fullStation;

        //! 1) 검색역에 포함되는 도보거리 존재하는 경우 -> 해당 도보거리 반환 
        if (distances.containsKey(fullStation)) {
            result = stationName.concat(" ").concat((String) distances.get(fullStation));
        //! 2) 존재하지 않는 경우 -> 최소 도보거리 반환 
        } else {
            int minDistance = Integer.MAX_VALUE;
            String minDistanceValue = null;

            for (Map.Entry<String, Object> entry : distances.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof String) {
                    String distanceValue = (String) value;
                    int distance = extractDistanceValue(distanceValue);

                    if (distance < minDistance) {
                        minDistance = distance;
                        minDistanceValue = distanceValue;
                        stationName = key;
                    }
                }
            }

            result = stationName.concat(" ").concat(minDistanceValue);
        }

        return result;
    }

    private static int extractDistanceValue(String distanceValue) {
        String[] parts = distanceValue.split(" ");
        int partLength = parts.length;

        String minuteString = parts[partLength - 1].substring(0, parts[partLength - 1].length() - 1);
        return Integer.parseInt(minuteString);
    }

}
