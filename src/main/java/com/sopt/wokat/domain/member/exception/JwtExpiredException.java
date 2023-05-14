package com.sopt.wokat.domain.member.exception;

import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.error.exception.BusinessException;

public class JwtExpiredException extends BusinessException {
    public JwtExpiredException() {
        super(ErrorCode.JWT_EXPIRED);
    }
}
