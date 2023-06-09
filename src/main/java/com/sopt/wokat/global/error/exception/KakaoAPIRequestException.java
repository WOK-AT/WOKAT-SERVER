package com.sopt.wokat.global.error.exception;

import com.sopt.wokat.global.error.ErrorCode;

public class KakaoAPIRequestException extends BusinessException {
    public KakaoAPIRequestException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }
    public KakaoAPIRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
