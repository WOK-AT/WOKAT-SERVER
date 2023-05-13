package com.sopt.wokat.mongoDB;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.sopt.wokat.domain.user.entity.User;

@SpringBootTest
public class insertTest {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void insertTest() {
        User user = User.builder()
            .userId("testID")
            .userPw("testPW")
            .build();

        //! User 저장
        mongoTemplate.insert(user);

        //! User 삭제
        String userId = user.getId();
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(userId)), User.class);
    }

}
