package com.sopt.wokat.domain.place.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.sopt.wokat.domain.place.dto.FilteringPlaceRequest;
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
    public OnePlaceInfoResponse findByIdCustom(String id) throws PlaceNotFoundException {
        SpaceInfo spaceInfo = mongoTemplate.findById(id, SpaceInfo.class);
        if (spaceInfo == null) throw new PlaceNotFoundException();

        OnePlaceInfoResponse placeInfoResponse = OnePlaceInfoResponse.createOnePlaceInfoResponse(spaceInfo);
        return placeInfoResponse;
    }

    @Override
    public List<SpaceInfo> findSpaceByProperties(Space space, String area, FilteringPlaceRequest filteringPlaceRequest) {
        Pageable pageable = PageRequest.of(filteringPlaceRequest.getPage(), 100, Sort.unsorted());
        Query query = new Query().with(pageable);

        List<Criteria> criteria = new ArrayList<>();
        
        //! 1) 카페/무료회의룸/무료공간 필터링
        criteria.add(Criteria.where("space").is(space));
        System.out.println(area);
        //! 2) 지역 필터링
        criteria.add(Criteria.where("area").is(area));

        /**
         * 
        //! 2) 거리순 필터링 
        if (filteringPlaceRequest.getFilter() == 0) {

            //? 1. Kakao 주소-좌표 변환 API => 지하철 역의 주소 좌표 변환 후, 좌표 이용해 지역 값 얻기 
                //? filteringPlaceRequest.getArea() -> 지하철역 
            //? 2. 같은 지역에 속하는 공간들을 검색 
            //? 3. 검색된 공간들의 도보거리 계산 후 정렬 
                //? 지하철역만 기준인 경우 - 단순 도보거리 가까운걸로
                //? 지역 기준인 경우 - 좌표 필요 

        //! 3) 북마크순 필터링 (2차 스프린트)
        } else {
        }

        //! 4) 인원-날짜 필터링 
        if (space == Space.MEETING_ROOM && 
                filteringPlaceRequest.getDate() != null &&
                filteringPlaceRequest.getHeadCount() != null) {
            //? 1. 휴무일 필터링 
            //? 2. 최대 인원수 필터링
            //? 3. 크롤링 통해 예약마감 필터링(가능한 장소만)
        }

         */
        
        query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        return mongoTemplate.find(query, SpaceInfo.class); 
    }

}
