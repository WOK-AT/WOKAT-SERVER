package com.sopt.wokat.domain.member.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.sopt.wokat.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Override
    public Member findByOauthID(String providerId) {
        Query query = new Query(Criteria.where("memberProfile.providerId").is(providerId));
        
        return mongoTemplate
                    .findOne(query, Member.class);
    }
    
}

