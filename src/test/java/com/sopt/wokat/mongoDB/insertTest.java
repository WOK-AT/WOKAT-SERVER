package com.sopt.wokat.mongoDB;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.sopt.wokat.domain.user.entity.User;
import com.sopt.wokat.domain.user.repository.UserRepository;

@SpringBootTest
public class insertTest {
    
    @Autowired
    MongoTemplate mongoTemplate;

    MongoOperations mongoOperations;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init() { mongoOperations = mongoTemplate; }

    @Test
    public void insertTest() {

        User user = User.builder()
            .userId("testID")
            .userPw("testPW")
            .build();
        

        //User findUser = userRepository.findByUserId(user.getUserId());
    }

}
