package com.sopt.wokat.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.sopt.wokat.domain.member.dto.MemberProfileQueryDTO;
import com.sopt.wokat.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    
    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Override
    public Member findByOauthID(String providerId) {
        LOGGER.info("providerId = {}", providerId);

        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.lookup("MemberProfile", "memberProfile", "_id", "memberProfile"),
            Aggregation.unwind("memberProfile"),
            Aggregation.match(Criteria.where("memberProfile.providerId").is(providerId))
        );
        
        List<Member> members = mongoTemplate.aggregate(aggregation, "Member", Member.class).getMappedResults();
        
        if (members.isEmpty()) {
            return null;
        } else {
            return members.get(0);
        }
    }

    @Override
    public Optional<MemberProfileQueryDTO> findMemberProfileById(String id) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("_id").is(id)),
            Aggregation.project()
                .andExpression("memberProfile.nickName").as("nickName")
                .andExpression("memberProfile.profileImage").as("profileImage")
                .andExpression("memberProfile.userEmail").as("userEmail")
        );

        List<MemberProfileQueryDTO> results = mongoTemplate.aggregate(
            aggregation, "Member", MemberProfileQueryDTO.class).getMappedResults();
        
        if (results.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(results.get(0));
        }
    }
    
}

