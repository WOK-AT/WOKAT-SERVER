package com.sopt.wokat.domain.user.entity;

import java.util.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import com.sopt.wokat.global.entity.BaseEntity;

@EqualsAndHashCode(callSuper=false)
@Entity @Data
// @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name= "users")
@Schema(description = "유저 테이블")
public class User extends BaseEntity {

}