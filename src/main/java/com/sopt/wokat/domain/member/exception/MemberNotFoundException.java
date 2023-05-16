package com.sopt.wokat.domain.member.exception;

import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.error.exception.BusinessException;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
