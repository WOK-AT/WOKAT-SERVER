package com.sopt.wokat.domain.place.exception;

import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.error.exception.BusinessException;

public class FileUploadException extends BusinessException {
    public FileUploadException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }
    public FileUploadException(ErrorCode errorCode) {
        super(errorCode);
    }
}
