package com.sopt.wokat.domain.member.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sopt.wokat.domain.member.entity.Member;

public interface MemberRepository extends MongoRepository<Member, String>, MemberRepositoryCustom {
}
