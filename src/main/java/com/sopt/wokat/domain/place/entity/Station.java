package com.sopt.wokat.domain.place.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "Station")
@Schema(description = "지하철역")
public class Station {

    @Id
    @Schema(description = "Member ID")
    private String id;

    @Field("line")
    @Schema(description = "호선")
    private String line;

    @Field("name")
    @Schema(description = "역 명")
    private String name;

    @Field("code")
    @Schema(description = "역 코드")
    private String code;

    @Field("lat")
    @Schema(description = "위도")
    private double latitude;

    @Field("lng")
    @Schema(description = "경도")
    private double longitude;

}
