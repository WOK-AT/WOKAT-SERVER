package com.sopt.wokat.domain.member.repository;

import java.io.IOException;
import java.util.Optional;

import com.sopt.wokat.domain.member.dto.MemberProfileQueryDTO;
import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.entity.MemberProfile;
import com.sopt.wokat.domain.member.entity.ProfileImage;

public interface MemberRepositoryCustom {
    
    Member findByOauthID(String providerId);
    
    Optional<MemberProfileQueryDTO> findMemberProfileById(String id);

    void saveMemberProfile(MemberProfile memberProfile);

    ProfileImage saveMemberProfileImage(String oauthURL) throws IOException;

    Member updateProfileImage(Member member, ProfileImage profileImage);

}
