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
import com.sopt.wokat.domain.member.dto.LoginResponse;
import com.sopt.wokat.domain.member.dto.OauthTokenResponse;
import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.repository.MemberRepository;
import com.sopt.wokat.global.config.security.provider.Oauth2MemberInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OauthService {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    private static final String AUTHORIZATION_TYPE = "Bearer";

    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Transactional
    public LoginResponse login(String providerName, String code){
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        OauthTokenResponse tokenResponse = getToken(code, provider);
        LOGGER.info("tokenResponse = {}", tokenResponse);

        Member member = 

    }

    //! 카카오 서버로 토큰 요청 
    private OauthTokenResponse getToken(String code, ClientRegistration provider) {
        return WebClient.create()
                    .post()
                    .uri(provider.getProviderDetails().getTokenUri())
                    .headers(header -> {
                        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                        header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                    })
                    .bodyValue(tokenRequest(code, provider))
                    .retrieve()
                    .bodyToMono(OauthTokenResponse.class)
                    .block();
    }

    private MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("client_id", provider.getClientId());
        return formData;
    }

    private Member getMemberProfile(String providerName, OauthTokenResponse tokenResponse, ClientRegistration provider) {
        Map<String, Object> userAttributes = getUserAttributes(provider, tokenResponse);
        Oauth2MemberInfo oauth2MemberInfo = 
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
