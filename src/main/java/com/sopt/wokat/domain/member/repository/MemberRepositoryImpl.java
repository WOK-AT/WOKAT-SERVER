package com.sopt.wokat.domain.member.repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.sopt.wokat.domain.member.dto.MemberProfileQueryDTO;
import com.sopt.wokat.domain.member.dto.ProfileImageUploadDTO;
import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.entity.MemberProfile;
import com.sopt.wokat.domain.member.entity.ProfileImage;
import com.sopt.wokat.infra.aws.S3ProfileUploader;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    
    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private S3ProfileUploader downlaodFileS3Uploader;

    public MemberRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    @Override
    public Member findByOauthID(String providerId) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.lookup("MemberProfile", "memberProfile.id", "id", "memberProfile"),
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

    @Override
    public void saveMemberProfile(MemberProfile memberProfile) {
        mongoTemplate.save(memberProfile);
    }

    //! S3 이미지 저장 후 DB 저장 
    @Override
    public ProfileImage saveMemberProfileImage(String oauthURL) throws IOException {
        ProfileImageUploadDTO imageUploadDTO = downlaodFileS3Uploader.uploadS3ProfileImage(oauthURL);
        ProfileImage profileImage = ProfileImage.createProfileImage(new ObjectId(), imageUploadDTO.getS3ImageURL(), oauthURL);

        mongoTemplate.save(profileImage);
        return profileImage;
    }

    @Override
    public Member updateProfileImage(Member member, ProfileImage profileImage) {
        Query memberQuery = new Query(Criteria.where("_id").is(member.getId()));

        //! 이전 프로필 이미지 데이터 삭제 후 업데이트
        Member beforeMember = mongoTemplate.findOne(memberQuery, Member.class);
        Query imageQuery = new Query(Criteria.where("_id").is(beforeMember.getProfileImage().getId()));
        
        //* S3 객체 삭제
        downlaodFileS3Uploader.deleteObject(member.getProfileImage().getS3URL().split("kakao/")[1]);

        //* 이전 이미지 객체 DB 삭제
        mongoTemplate.remove(imageQuery, ProfileImage.class);

        //* 새로운 이미지 객체 DB 업데이트
        Update deleteUpdate = new Update().unset("profileImage");
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);

        Member updatedMember = mongoTemplate.findAndModify(memberQuery, deleteUpdate, options, Member.class);

        //* 새로운 profile image 업데이트 
        updatedMember.setProfileImage(profileImage);
        return updatedMember;
    }

}

