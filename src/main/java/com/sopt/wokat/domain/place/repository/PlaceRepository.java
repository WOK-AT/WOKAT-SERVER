package com.sopt.wokat.domain.place.repository;

import com.sopt.wokat.domain.place.entity.SpaceInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PlaceRepository extends MongoRepository<SpaceInfo, String>, PlaceRepositoryCustom {


}
