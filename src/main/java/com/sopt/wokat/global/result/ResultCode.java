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

    ;

    private final int status;
    private final String code;
    private final String message;

}
