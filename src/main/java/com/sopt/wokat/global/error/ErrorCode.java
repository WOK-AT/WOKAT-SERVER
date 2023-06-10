package com.sopt.wokat.global.error;

import org.apache.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ErrorCode Convention
 * - 도메인 별로 나누어 관리
 * - [주체_이유] 형태로 생성
 * - 코드는 도메인명 앞에서부터 1~2글자로 사용
 * - 메시지는 "~~다."로 마무리
 */

@Getter
@AllArgsConstructor
public enum ErrorCode {
    
    //! Global
	INTERNAL_SERVER_ERROR(500, "G001", "내부 서버 오류입니다."),
	METHOD_NOT_ALLOWED(405, "G002", "허용되지 않은 HTTP method입니다."),
	INVALID_INPUT_VALUE(400, "G003", "유효하지 않은 입력입니다."),
	INVALID_INPUT_TYPE(400, "G004", "입력 타입이 유효하지 않습니다."),
	HTTP_MESSAGE_NOT_READABLE(400, "G005", "request message body가 없거나, 값 타입이 올바르지 않습니다."),
	HTTP_HEADER_INVALID(400, "G006", "request header가 유효하지 않습니다."),
	ENTITY_TYPE_INVALID(500, "G007", "올바르지 않은 entity type 입니다."),
    
    //! JWT
    UNAUTHORIZED_USER(401, "J001","로그인이 필요합니다."),
    FORBIDDEN_USER(401, "J002", "권한이 없는 요청입니다"),
    UNAUTHORIZED_ACCESS_TOKEN(401, "J003", "유효하지 않은 Access Token입니다."),
    UNAUTHORIZED_REFRESH_TOKEN(401, "J004", "유효하지 않은 Refresh Token입니다."),
    
    //! Member
    MEMBER_NOT_FOUND(400, "M001","존재하지 않는 유저입니다."),

    //! Place
    INVALID_FILE(400, "P001", "올바르지 않은 파일입니다."),
    PLACE_NOT_FOUND(400, "P002", "존재하지 않는 공간입니다."),


    //! Open API
    COORDS_TO_LOCATION_FAIL(500, "O001", "좌표 주소변환 실패"),
    LOCATION_TO_COORDS_FAIL(500, "O002", "주소 좌표변환 실패"),
    GET_WALK_DISTANCE_FAIL(500, "O003", "도보거리 조회 실패")
    ;

    private final int status;
    private final String code;
    private final String message;

}
