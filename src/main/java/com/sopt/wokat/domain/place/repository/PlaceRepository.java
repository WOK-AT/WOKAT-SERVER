package com.sopt.wokat.domain.place.repository;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.sopt.wokat.domain.place.entity.Space;
import com.sopt.wokat.domain.place.entity.SpaceInfo;

public interface PlaceRepository extends MongoRepository<SpaceInfo, String>, PlaceRepositoryCustom {

    List<SpaceInfo> findSpaceByProperties(String area, Space space, Map<String, Object> openTime, String headCount,
            PageRequest of);

}
