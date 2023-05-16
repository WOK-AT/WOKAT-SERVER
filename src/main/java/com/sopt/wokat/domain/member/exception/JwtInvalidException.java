package com.sopt.wokat.domain.member.exception;

import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.error.exception.BusinessException;

public class JwtInvalidException extends BusinessException {
    public JwtInvalidException() {
        super(ErrorCode.UNAUTHORIZED_ACCESS_TOKEN);
    }

    public JwtInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
