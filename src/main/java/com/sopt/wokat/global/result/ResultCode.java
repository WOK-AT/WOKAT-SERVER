package com.sopt.wokat.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ResultCode Convention
 * - 도메인 별로 나누어 관리
 * - [동사_목적어_SUCCESS] 형태로 생성
 * - 코드는 도메인명 앞에서부터 1~2글자로 사용
 * - 메시지는 "~~다."로 마무리
 */

@Getter
@AllArgsConstructor
public enum ResultCode {
    
    // User
    REGISTER_SUCCESS(200, "M001", "회원가입 되었습니다."),
    LOGIN_SUCCESS(200, "M002", "로그인에 성공했습니다."),
    LOGIN_FAIL(500, "M003", "로그인에 실패했습니다."),
    REFRESH_SUCCESS(200, "M004", "재발급 되었습니다."),
    LOGOUT_SUCCESS(200, "M005", "로그아웃 되었습니다."),

    // Place
    POST_PLACE_SUCCESS(200, "P001", "장소 등록에 성공했습니다."),
    POST_PLACE_FAIL(500, "P002", "장소 등록에 실패했습니다."),
    GET_PLACE_LIST_SUCCESS(200, "P003", "장소 필터링 조회에 성공했습니다."),
    GET_PLACE_LIST_FAIL(500, "P004", "장소 필터링 조회에 실패했습니다."),
    GET_PLACE_SUCCESS(200, "P005", "특정 장소 조회에 성공했습니다."),
    GET_PLACE_FAIL(400, "P006", "특정 장소 조회에 실패했습니다."),
    GET_PLACE_ADDRESS_SUCCESS(200, "P007", "변환된 주소 조회에 성공했습니다."),
    GET_PLACE_ADDRESS_FAIL(400, "P008", "변환된 주소 조회에 실패했습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;

}
