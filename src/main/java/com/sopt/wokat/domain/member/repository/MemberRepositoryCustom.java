package com.sopt.wokat.domain.member.repository;

import java.io.IOException;
import java.util.Optional;

import com.sopt.wokat.domain.member.dto.MemberProfileQueryDTO;
import com.sopt.wokat.domain.member.dto.OauthResponse;
import com.sopt.wokat.domain.member.entity.Member;

public interface MemberRepositoryCustom {
    
    Member setProfileImage(OauthResponse oauthResponse) throws IOException;

    Member updateProfileImage(Member member, String oauthProfileImageURL) throws IOException;

}
