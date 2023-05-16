package com.sopt.wokat.global.util;

import java.security.Key;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.util.StandardCharset;
import com.sopt.wokat.domain.member.exception.JwtInvalidException;
import com.sopt.wokat.global.entity.Token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    
    @Value("${jwt.access-token.expire-length}")
	private long ACCESS_TOKEN_EXPIRES;

	@Value("${jwt.refresh-token.expire-length}")
	private long REFRESH_TOKEN_EXPIRES;

    private final Key JWT_KEY;

    public JwtTokenProvider(@Value("${jwt.token.secret-key}") byte[] key) {
		this.JWT_KEY = Keys.hmacShaKeyFor(key);
	}

    public Token createAccessToken(String payload) {
        String token = createToken(payload, ACCESS_TOKEN_EXPIRES);
        return new Token(token, ACCESS_TOKEN_EXPIRES);
    }

    public Token createRefreshToken() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);

        String generatedString = new String(array, StandardCharset.UTF_8);
        String token = createToken(generatedString, REFRESH_TOKEN_EXPIRES);
        
        return new Token(token, REFRESH_TOKEN_EXPIRES);
    }

    public String createToken(String payload, long expireLength) {
        Claims claims = Jwts.claims().setSubject(payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(JWT_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getPayload(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(JWT_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            throw new JwtInvalidException();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimJws = Jwts.parserBuilder()
                .setSigningKey(JWT_KEY)
                .build()
                .parseClaimsJws(token);
            
            return !claimJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

}
