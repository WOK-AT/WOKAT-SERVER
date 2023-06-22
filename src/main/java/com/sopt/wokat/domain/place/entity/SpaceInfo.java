package com.sopt.wokat.domain.place.entity;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.sopt.wokat.domain.place.dto.PostPlaceRequest;
import com.sopt.wokat.global.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "SpaceInfo")
@Schema(description = "공간 정보 테이블")
public class SpaceInfo extends BaseEntity {

    @Id
    @Schema(description = "Member ID")
    private String id;

    @Field("space_class")
    @Schema(description = "공간 분류")
    private Space space;

    @Field("space_area")
    @Schema(description = "지역")
    private String area;

    @Field("homepage_url")
    @Schema(description = "홈페이지 주소")
    @Builder.Default
    private String homepageURL = null;

    @Field("space_name")
    @Schema(description = "공간명")
    private String name;

    @Field("space_charge")
    @Schema(description = "유/무료")
    @Builder.Default
    private String isFree = null;

    @Field("booking_url")
    @Schema(description = "대관 신청 링크")
    @Builder.Default
    private String bookingURL = null;

    @Field("space_socket")
    @Schema(description = "콘센트 정보")
    @Builder.Default
    private String socket = null;

    @Field("space_parking")
    @Schema(description = "주차공간 정보")
    @Builder.Default
    private String parkingLot = null;

    @Field("space_hdmi_screen")
    @Schema(description = "HDMI/스크린 정보")
    @Builder.Default
    private String hdmiScreen = null;

    @Field("space_roadName")
    @Schema(description = "도로명 주소")
    private String locationRoadName;

    @Field("space_lotNumber")
    @Schema(description = "지번 주소")
    private String locationLotNumber;

    @Field("space_headCount")
    @Schema(description = "수용 인원수")
    @Builder.Default
    private String headCount = null;

    @Field("space_introduce")
    @Schema(description = "공간 소개")
    @Builder.Default
    private List<String> introduction = null;

    @Field("space_contact")
    @Schema(description = "전화번호")
    @Builder.Default
    private List<String> contact = null;

    @Field("space_hashTag")
    @Schema(description = "해쉬태그")
    @Builder.Default
    private List<String> hashTags = null;

    @Field("space_wifi")
    @Schema(description = "와이파이 ID/PW")
    @Builder.Default
    private Map<String, Object> wifi = null;     //! [ID: 아이디, PW: 비밀번호]

    @Field("space_time")
    @Schema(description = "운영 시간")
    @Builder.Default
    private Map<String, Object> openTime = null;  //! [요일: 운영시간]

    @Field("space_distance")
    @Schema(description = "도보거리")
    @Builder.Default
    private Map<String, Object> distance = null;   //! [역이름: 도보거리]

    @Field("space_images")
    @Schema(description = "S3 이미지 URL")
    private List<String> imageURLs;

    @Field("station_distance")                      //! [역이름: 역까지 도보거리]
    @Schema(description = "서울 자치구 별 지하철역~공간 도보거리")
    private Map<String, Object> walkTime;

    public static SpaceInfo createSpaceInfo (PostPlaceRequest placeRequest) {
        SpaceInfo spaceInfo = SpaceInfo.builder()
                .space(Space.fromValue(placeRequest.getSpaceClass().toString()))
                .area(placeRequest.getArea())
                .homepageURL(placeRequest.getHomepageURL())
                .name(placeRequest.getName())
                .isFree(placeRequest.getIsFree())
                .bookingURL(placeRequest.getBookingURL())
                .socket(placeRequest.getSocket())
                .parkingLot(placeRequest.getParkingLot())
                .hdmiScreen(placeRequest.getHdmiScreen())
                .locationRoadName(placeRequest.getLocationRoadName())
                .locationLotNumber(placeRequest.getLocationLotNumber())
                .headCount(placeRequest.getHeadCount())
                .introduction(placeRequest.getIntroduction())
                .contact(placeRequest.getContact())
                .hashTags(placeRequest.getHashTags())
                .wifi(placeRequest.getWifi())
                .openTime(placeRequest.getOpenTime())
                .distance(placeRequest.getDistance())
                .build();

        return spaceInfo;
    }

}