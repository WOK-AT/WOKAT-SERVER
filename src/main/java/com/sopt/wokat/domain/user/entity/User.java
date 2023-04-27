package com.sopt.wokat.domain.user.entity;


import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.sopt.wokat.global.entity.BaseEntity;


@Getter
@AllArgsConstructor()
@Document("user")
@Schema(description = "유저 테이블")
public class User extends BaseEntity {
    
    private String userId;
    private String userPw;

}


