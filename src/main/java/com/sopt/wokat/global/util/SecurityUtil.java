package com.sopt.wokat.global.util;

import org.springframework.security.core.context.SecurityContextHolder;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SecurityUtil {
    
    public static String getCurrentMemberId() {
        MemberAuthentication memberAuthentication = (MemberAuthentication) SecurityContextHolder.getContext().getAuthentication();;
        return memberAuthentication.getMemberId();
    }
    
}
