package com.sopt.wokat.global.result;

import lombok.Getter;

@Getter
public class ResultResponse {
    
    private int status;
    private String code;
    private String message;
    private Object data;

    public ResultResponse(ResultCode resultCode, Object data) {
        this.status = resultCode.getStatus();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data= data;
    }

    public static ResultResponse of(ResultCode resultCode, Object data) {
        return new ResultResponse(resultCode, data);
    }

    public static ResultResponse of(ResultCode resultCode) {
		return new ResultResponse(resultCode, "");
	}

}
