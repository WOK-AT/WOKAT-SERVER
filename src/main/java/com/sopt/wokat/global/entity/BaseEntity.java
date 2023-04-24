package com.sopt.wokat.global.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "기본 컬럼")
@Getter @Setter 
public class BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "유저 고유 ID")
    private Long id;

    @CreatedDate
    @Column(name = "created_at")
    @Schema(description = "생성 일자")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @Schema(description = "업데이트 일자")
    private LocalDateTime updatedAt;

}
