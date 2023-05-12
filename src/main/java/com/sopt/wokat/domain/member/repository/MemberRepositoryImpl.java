package com.sopt.wokat.domain.member.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MemberRepositoryImpl implements MemberRepositoryCustom {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
}

