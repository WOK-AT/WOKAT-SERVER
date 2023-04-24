package com.sopt.wokat.global.error.exception;

import java.util.ArrayList;
import java.util.List;

import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.error.ErrorResponse;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    
    private ErrorCode errorCode;
    private List<ErrorResponse.FieldError> errors = new ArrayList<>();
    
    public BusinessException(String message,ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, List<ErrorResponse.FieldError> errors) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errors = errors;
    } 
    

}
