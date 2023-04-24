package com.sopt.wokat.domain.user.exception;

import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.error.exception.BusinessException;

public class JwtInvalidException extends BusinessException {
    public JwtInvalidException() {
        super(ErrorCode.JWT_INVALID);
    }

    public JwtInvalidException(String message) {
        super(message, ErrorCode.JWT_INVALID);
    }
}
