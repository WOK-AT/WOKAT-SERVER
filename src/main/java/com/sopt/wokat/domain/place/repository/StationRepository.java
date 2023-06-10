package com.sopt.wokat.domain.place.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sopt.wokat.domain.place.entity.Station;

public interface StationRepository extends MongoRepository<Station, String> {
    List<Station> findByName(String name);
}
