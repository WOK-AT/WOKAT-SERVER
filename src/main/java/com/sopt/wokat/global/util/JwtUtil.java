package com.sopt.wokat.global.util;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.sopt.wokat.domain.member.dto.JwtDTO;
import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.exception.JwtExpiredException;
import com.sopt.wokat.domain.member.exception.JwtInvalidException;
import com.sopt.wokat.global.config.security.token.JwtAuthenticationToken;
import com.sopt.wokat.global.error.exception.BusinessException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    //! Authorization 헤더에서 Bearer 토큰 추출하기 
    public String extractJWT(String authenticationHeader) {
        if (authenticationHeader == null) {
            throw new JwtInvalidException("Authorization 헤더가 존재하지 않습니다.");
        } else if (!authenticationHeader.startsWith(BEARER_TYPE_PREFIX)) {
            throw new JwtInvalidException("Bearer 타입의 토큰이 아닙니다.");
        }
        return  authenticationHeader.split(" ")[1];
    }

    public Authentication getAuthentication(String token) throws BusinessException {
        Claims claims = parseClaims(token);

        final List<SimpleGrantedAuthority> authorities = Arrays.stream(
                        claims.get(CLAIM_AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        final User principal = new User((String)claims.get(CLAIM_MEMBER_ID_KEY), "", authorities);
        
        return JwtAuthenticationToken.of(principal, token, authorities);
    }

    public JwtDTO generateJwtDTO(Authentication authentication) {
        final String authoritiesString = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","));
        final long currentTime = (new Date()).getTime();

        final Date accessTokenExpiresIn = new Date(currentTime + ACCESS_TOKEN_EXPIRES);
        final Date refreshTokenExpiresIn = new Date(currentTime + REFRESH_TOKEN_EXPIRES);

        final String accessToken = Jwts.builder()
                            .setSubject(ACCESS_TOKEN_SUBJECT)
                            .claim(CLAIM_MEMBER_ID_KEY, authentication.getName())
                            .claim(CLAIM_AUTHORITIES_KEY, authoritiesString)
                            .claim(CLAIM_JWT_TYPE_KEY, BEARER_TYPE)
                            .setExpiration(accessTokenExpiresIn)
                            .signWith(JWT_KEY, SignatureAlgorithm.HS512)
                            .compact();

        final String refreshToken = Jwts.builder()
                            .setSubject(REFRESH_TOKEN_SUBJECT)
                            .claim(CLAIM_MEMBER_ID_KEY, authentication.getName())
                            .claim(CLAIM_AUTHORITIES_KEY, authoritiesString)
                            .setExpiration(refreshTokenExpiresIn)
                            .signWith(JWT_KEY, SignatureAlgorithm.HS512)
                            .compact();

        return JwtDTO.builder()
                        .type(BEARER_TYPE)
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
    }

    public JwtDTO generateJwtDTO(Member member) {
        final String authoritiesString = member.getRole().toString();
        long currentTime = (new Date()).getTime();

        final Date accessTokenExpiresIn = new Date(currentTime + ACCESS_TOKEN_EXPIRES);
		final Date refreshTokenExpiresIn = new Date(currentTime + REFRESH_TOKEN_EXPIRES);

		final String accessToken = Jwts.builder()
				.setSubject(ACCESS_TOKEN_SUBJECT)
				.claim(CLAIM_MEMBER_ID_KEY, member.getId().toString())
				.claim(CLAIM_AUTHORITIES_KEY, authoritiesString)
				.claim(CLAIM_JWT_TYPE_KEY, BEARER_TYPE)
				.setExpiration(accessTokenExpiresIn)
				.signWith(JWT_KEY, SignatureAlgorithm.HS512)
				.compact();

		final String refreshToken = Jwts.builder()
				.setSubject(REFRESH_TOKEN_SUBJECT)
				.claim(CLAIM_MEMBER_ID_KEY, member.getId().toString())
				.claim(CLAIM_AUTHORITIES_KEY, authoritiesString)
				.setExpiration(refreshTokenExpiresIn)
				.signWith(JWT_KEY, SignatureAlgorithm.HS512)
				.compact();

		return JwtDTO.builder()
				.type(BEARER_TYPE)
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
    }

    private Claims parseClaims(String token) throws BusinessException {
        try {
            return Jwts.parserBuilder().setSigningKey(JWT_KEY).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException();
        } catch (Exception e) {
            throw new JwtInvalidException();
        }
    }

}
