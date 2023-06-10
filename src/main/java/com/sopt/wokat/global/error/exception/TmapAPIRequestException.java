package com.sopt.wokat.global.error.exception;

import com.sopt.wokat.global.error.ErrorCode;

public class TmapAPIRequestException extends BusinessException {
    public TmapAPIRequestException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }
    public TmapAPIRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
