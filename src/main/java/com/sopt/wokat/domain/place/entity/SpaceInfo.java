package com.sopt.wokat.domain.place.entity;

import java.util.List;
import java.util.Map;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.lang.Nullable;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.util.BsonUtils;

@Data
@EqualsAndHashCode(callSuper=false)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "SpaceInfo")
@Schema(description = "공간 정보 테이블")
@Builder
public class SpaceInfo {

    @MongoId
    private ObjectId id;

    @Field("공간 분류")
    @Schema(description = "공간 분류")
    private Space space;

    @Field("지역")
    @Schema(description = "지역")
    private String area;

    @Field("홈페이지 주소") @Nullable
    @Schema(description = "홈페이지 주소")
    private String homepageURL;
    
    @Field("무료 공간명")
    @Schema(description = "공간명")
    private String name;

    @Field("유/무료 체크 확인") @Nullable
    @Schema(description = "유/무료")
    private Boolean isFree;

    @Field("예약 필수 여부") @Nullable
    @Schema(description = "예약 필수 여부")
    private String isRequiredReserve;

    @Field("공간소개") @Nullable
    @Schema(description = "공간 소개")
    private List<String> introduction;

    @Field("공간 사진") @Nullable
    @Schema(description = "공간 사진")
    private List<String> image;

    @Field("운영 시간") @Nullable
    @Schema(description = "운영 시간")
    //private Map<String, Object> openTime;  //! [요일: 운영시간]
    private String openTime;

    @Field("전화번호") @Nullable
    @Schema(description = "전화번호")
    private List<String> contact;

    @Field("WIFI ID / PW") @Nullable
    @Schema(description = "와이파이 ID/PW")
    //private Map<String, Object> wifi;      //! [ID: 아이디, PW: 비밀번호]
    private String wifi;

    @Field("콘센트") @Nullable
    @Schema(description = "콘센트 정보")
    private String socket;

    @Field("주차공간 여부") @Nullable
    @Schema(description = "주차공간 정보")
    private String parkingLot;

    @Field("HDMI/스크린 여부") @Nullable
    @Schema(description = "HDMI/스크린 정보")
    private String hdmiScreen;

    @Field("공간 도로명 주소") @Nullable
    @Schema(description = "도로명 주소")
    private String locationRoadName;

    @Field("공간 지번 주소") @Nullable
    @Schema(description = "지번 주소")
    private String locationLotNumber;

    @Field("최대 수용 인원") @Nullable
    @Schema(description = "수용 인원수")
    private String headCount;

    @Field("해쉬태그") @Nullable
    @Schema(description = "해쉬태그")
    private List<String> hashTags; 

    @Field("지하철역에서 도보 몇분 ") @Nullable
    @Schema(description = "도보거리")
//    private Map<String, Object> distance;   //! [역이름: 도보거리]
    private String distance;


}
