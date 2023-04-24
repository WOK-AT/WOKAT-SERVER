package com.sopt.wokat.global.error;

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
    JWT_INVALID(40, "J001", "유효하지 않은 토큰입니다."),
    JWT_EXPIRED(410, "J002", "만료된 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(401, "J003", "만료된 Refresh 토큰입니다. 재로그인이 필요합니다."),

    ;

    private final int status;
    private final String code;
    private final String message;

}
