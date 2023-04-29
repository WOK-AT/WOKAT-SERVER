package com.sopt.wokat.domain.user.repository;

import java.util.*;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sopt.wokat.domain.user.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public List<User> findByUserId(String userId);
}
