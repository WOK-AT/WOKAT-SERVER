package com.sopt.wokat.domain.member.repository;

import com.sopt.wokat.domain.member.entity.Member;

public interface MemberRepositoryCustom {
    
    Member findByOauthID(String providerId);
    
}
