package com.sopt.wokat.global.util;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    
    private final static String CLAIM_AUTHORITIES_KEY = "authorities";
	private final static String CLAIM_JWT_TYPE_KEY = "type";
	private final static String CLAIM_MEMBER_ID_KEY = "userId";
	private final static String BEARER_TYPE_PREFIX = "Bearer ";
	private final static String BEARER_TYPE = "Bearer";
	private final static String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private final static String REFRESH_TOKEN_SUBJECT = "RefreshToken";
	private final Key JWT_KEY;
	
    @Value("${jwt.access-token.expire-length}")
	private long ACCESS_TOKEN_EXPIRES;
	@Value("${jwt.refresh-token.expire-length}")
	private long REFRESH_TOKEN_EXPIRES;

    public JwtUtil(@Value("${jwt.token.secret-key}") byte[] key) {
        this.JWT_KEY = Keys.hmacShaKeyFor(key);
    }
    
}
