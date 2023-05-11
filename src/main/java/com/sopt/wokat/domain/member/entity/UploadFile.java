package com.sopt.wokat.domain.member.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "Profile")
@Schema(description = "Member_Profile 테이블")
public class UploadFile {
    
}
