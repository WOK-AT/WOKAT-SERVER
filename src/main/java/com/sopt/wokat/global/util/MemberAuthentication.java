package com.sopt.wokat.global.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class MemberAuthentication extends AbstractAuthenticationToken {

    private static final String USER = "USER";

    private final String memberId;

    public MemberAuthentication(String memberId) {
        super(authorities());
        this.memberId = memberId;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return memberId;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    public String getMemberId() {
        return memberId;
    }
    
    private static List<GrantedAuthority> authorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER));
        return authorities;
    }
    
}
