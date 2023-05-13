package com.sopt.wokat.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Client;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import com.nimbusds.oauth2.sdk.TokenRequest;
import com.sopt.wokat.domain.member.dto.AuthorizationRequest;
import com.sopt.wokat.domain.member.dto.LoginResponse;
import com.sopt.wokat.domain.member.dto.OauthTokenResponse;
import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.repository.MemberRepository;
import com.sopt.wokat.global.config.redis.RedisUtil;

//import com.sopt.wokat.global.config.security.provider.Oauth2MemberInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OauthService {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    private static final String AUTHORIZATION_TYPE = "Bearer";

    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final MemberRepository memberRepository;
    //private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    
    /**
     * @InMemoryRepository application-oauth properties 정보를 담고 있음
     * @getToken() 넘겨받은 code 로 Oauth 서버에 Token 요청
     * @getUserProfile 첫 로그인 시 회원가입
     * 유저 인증 후 Jwt AccessToken, Refresh Token 생성
     * RefreshToken Redis 저장 만료기간 1달
     */
    /* 
    @Transactional
    public LoginResponse login(AuthorizationRequest authorizationRequest) {
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(authorizationRequest.getProviderName());
        LOGGER.info("tokenResponse = {}", tokenResponse);

        Member member = getMemberProfile(authorizationRequest, provider);

    }
    */
/* 
    private Member getMemberProfile(AuthorizationRequest authorizationRequest, ClientRegistration provider) {
        OauthTokenResponse token = getToken(authorizationRequest, provider);
        Map<String, Object> userAttributes = getUserAttributes(provider, token);
        Member extract = 
    }
*/
    //! 카카오 서버로 토큰 요청 
    private OauthTokenResponse getToken(AuthorizationRequest authorizationRequest, ClientRegistration provider) {
        return WebClient.create()
                    .post()
                    .uri(provider.getProviderDetails().getTokenUri())
                    .headers(header -> {
                        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                        header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                    })
                    .bodyValue(tokenRequest(authorizationRequest, provider))
                    .retrieve()
                    .bodyToMono(OauthTokenResponse.class)
                    .block();
    }

    private MultiValueMap<String, String> tokenRequest(AuthorizationRequest authorizationRequest, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", authorizationRequest.getCode());
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("client_id", provider.getClientId());
        return formData;
    }

    //! 카카오 서버에서 유저 정보 가져오기 
    private Map<String, Object> getUserAttributes(ClientRegistration provider, OauthTokenResponse tokenResponse) {
        return WebClient.create()
                    .get()
                    .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                    .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
    }

}
