package com.sopt.wokat.domain.place.entity;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.lang.Nullable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "SpaceInfo")
@Schema(description = "공간 정보 테이블")
public class SpaceInfo {

    @Field("space_class")
    @Schema(description = "공간 분류")
    private Space space;

    @Field("space_area")
    @Schema(description = "지역")
    private String area;

    @Field("homepage_url") @Nullable
    @Schema(description = "홈페이지 주소")
    private String homepageURL;
    
    @Field("space_name")
    @Schema(description = "공간명")
    private String name;

    @Field("space_charge") @Nullable
    @Schema(description = "유/무료")
    private Boolean isFree;

    @Field("space_reserve") @Nullable
    @Schema(description = "예약 필수 여부")
    private Boolean isRequiredReserve;

    @Field("space_introduce") @Nullable
    @Schema(description = "공간 소개")
    private List<String> introduction;

    @Field("space_image") @Nullable
    @Schema(description = "공간 사진")
    private List<String> image;

    @Field("space_time") @Nullable
    @Schema(description = "운영 시간")
    private Map<String, Object> openTime;

    @Field("space_contact") @Nullable
    @Schema(description = "전화번호")
    private List<String> contact;

    @Field("space_wifi") @Nullable
    @Schema(description = "와이파이 ID/PW")
    private Map<String, Object> wifi;

    @Field("space_socket") @Nullable
    @Schema(description = "콘센트 정보")
    private String socket;

    @Field("space_parking") @Nullable
    @Schema(description = "주차공간 정보")
    private String parkingLot;

    @Field("space_hdmi_screen") @Nullable
    @Schema(description = "HDMI/스크린 정보")
    private Boolean hdmiScreen;

    @Field("space_roadName") @Nullable
    @Schema(description = "도로명 주소")
    private String locationRoadName;

    @Field("space_") @Nullable
    @Schema(description = "지번 주소")
    private String locationLotNumber;

    @Field("space_headCount") @Nullable
    @Schema(description = "수용 인원수")
    private String headCount;

    @Field("space_hashTag") @Nullable
    @Schema(description = "해쉬태그")
    private List<String> hashTags;

    @Field("space_distance") @Nullable
    @Schema(description = "도보거리")
    private Map<String, Object> distance;

}
