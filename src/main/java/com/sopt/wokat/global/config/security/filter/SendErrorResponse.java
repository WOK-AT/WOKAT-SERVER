package com.sopt.wokat.global.config.security.filter;

import com.sopt.wokat.global.error.ErrorCode;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendErrorResponse {
    
    private int status;
    private String code;
    private String message;

    public SendErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
    }

    public static SendErrorResponse of(ErrorCode errorCode) {
        return new SendErrorResponse(errorCode);
    }

}
