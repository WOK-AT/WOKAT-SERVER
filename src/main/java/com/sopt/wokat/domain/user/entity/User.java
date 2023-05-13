package com.sopt.wokat.domain.user.entity;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import nonapi.io.github.classgraph.json.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sopt.wokat.global.entity.BaseEntity;

@Data
@Document("User")
@Schema(description = "유저 테이블")
public class User extends BaseEntity {
    
    @Id
    private String id;

    private String userId;
    private String userPw;

    @Builder
    public User(String userId, String userPw) {
        this.userId = userId;
        this.userPw = userPw;
        
    }

    public String getId(){
        return id;
    }

}


