package com.sopt.wokat.domain.member.repository;

import java.util.Optional;

import com.sopt.wokat.domain.member.dto.MemberProfileQueryDTO;
import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.entity.MemberProfile;

public interface MemberRepositoryCustom {
    
    Member findByOauthID(String providerId);
    
    void saveMemberProfile(MemberProfile memberProfile);

    Optional<MemberProfileQueryDTO> findMemberProfileById(String id);
    
}
