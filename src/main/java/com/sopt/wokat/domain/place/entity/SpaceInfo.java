package com.sopt.wokat.domain.place.entity;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.sopt.wokat.global.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "SpaceInfo")
@Schema(description = "공간 정보 테이블")
public class SpaceInfo extends BaseEntity {

    @Field("space_class")
    @Schema(description = "공간 분류")
    private Space space;

    @Field("space_area")
    @Schema(description = "지역")
    private String area;

    @Field("homepage_url")
    @Schema(description = "홈페이지 주소")
    private String homepageURL;
    
    @Field("space_name")
    @Schema(description = "공간명")
    private String name;

    @Field("space_charge")
    @Schema(description = "유/무료")
    private Boolean isFree;

    @Field("space_reserve")
    @Schema(description = "예약 필수 여부")
    private Boolean isRequiredReserve;

    @Field("space_introduce")
    @Schema(description = "공간 소개")
    private List<String> introduction;

    @Field("space_image")
    @Schema(description = "공간 사진")
    private List<String> image;

    @Field("space_time")
    @Schema(description = "운영 시간")
    private Map<String, Object> openTime;  //! [요일: 운영시간]

    @Field("space_contact")
    @Schema(description = "전화번호")
    private List<String> contact;

    @Field("space_wifi")
    @Schema(description = "와이파이 ID/PW")
    private Map<String, Object> wifi;      //! [ID: 아이디, PW: 비밀번호] 

    @Field("space_socket")
    @Schema(description = "콘센트 정보")
    private String socket;

    @Field("space_parking")
    @Schema(description = "주차공간 정보")
    private String parkingLot;

    @Field("space_hdmi_screen")
    @Schema(description = "HDMI/스크린 정보")
    private Boolean hdmiScreen;

    @Field("space_roadName")
    @Schema(description = "도로명 주소")
    private String locationRoadName;

    @Field("space_") 
    @Schema(description = "지번 주소")
    private String locationLotNumber;

    @Field("space_headCount")
    @Schema(description = "수용 인원수")
    private String headCount;

    @Field("space_hashTag")
    @Schema(description = "해쉬태그")
    private List<String> hashTags; 

    @Field("space_distance")
    @Schema(description = "도보거리")
    private Map<String, Object> distance;   //! [역이름: 도보거리]

}
