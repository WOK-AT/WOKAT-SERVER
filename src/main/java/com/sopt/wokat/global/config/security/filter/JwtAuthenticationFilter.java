package com.sopt.wokat.global.config.security.filter;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sopt.wokat.domain.member.dto.LoginMember;
import com.sopt.wokat.domain.member.service.AuthService;
import com.sopt.wokat.global.config.security.token.AuthorizationExtractor;
import com.sopt.wokat.global.error.exception.BusinessException;
import com.sopt.wokat.global.util.JwtTokenProvider;
import com.sopt.wokat.global.util.MemberAuthentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getMethod().equals("OPTIONS")) { return; }
        try {
            String accessToken = AuthorizationExtractor.extract(request);

            if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
                LoginMember loginMember = authService.findMemberByAccessToken(accessToken);
                System.out.println(loginMember);
                MemberAuthentication authentication = new MemberAuthentication(loginMember.getId());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (BusinessException e) {
            request.setAttribute("BusinessException", e);
        }
        filterChain.doFilter(request, response);
    }
    
}
