package com.sopt.wokat.domain.member.repository;

import java.util.Optional;

import com.sopt.wokat.domain.member.dto.MemberProfileQueryDTO;
import com.sopt.wokat.domain.member.entity.Member;

public interface MemberRepositoryCustom {
    
    Member findByOauthID(String providerId);
    
    Optional<MemberProfileQueryDTO> findMemberProfileById(String id);
    
}
