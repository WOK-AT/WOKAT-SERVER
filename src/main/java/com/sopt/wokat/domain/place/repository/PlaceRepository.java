package com.sopt.wokat.domain.place.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.sopt.wokat.domain.place.entity.SpaceInfo;

public interface PlaceRepository extends MongoRepository<SpaceInfo, String>, PlaceRepositoryCustom {
}
