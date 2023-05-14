package com.sopt.wokat.DB;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.entity.MemberProfile;
import com.sopt.wokat.domain.member.entity.Role;

@SpringBootTest
public class MemberTest {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void insertTest() {
        MemberProfile profile = MemberProfile.builder()
                            .id(new ObjectId())
                            .nickName("TestMember")
                            .profileImage(null)
                            .userEmail("test@naver.com")
                            .provider("kakao")
                            .providerId("test")
                            .build();
        
        Member member = Member.builder()
                            .memberProfile(profile)
                            .role(Role.ROLE_MEMBER)
                            .build(); 

        //! Member 저장
        mongoTemplate.save(profile);
        mongoTemplate.save(member);

        //! Member 삭제
        String memberProfileId = profile.getId().toString();
        String memberId = member.getId();        

        Query profileQuery = new Query(Criteria.where("_id").is(memberProfileId));
        Query memberQuery = new Query(Criteria.where("_id").is(memberId));

        mongoTemplate.remove(profileQuery, MemberProfile.class);
        mongoTemplate.remove(memberQuery, Member.class);
    }

}
