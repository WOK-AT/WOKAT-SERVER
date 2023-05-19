package com.sopt.wokat.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import com.sopt.wokat.domain.member.dto.AuthorizationRequest;
import com.sopt.wokat.domain.member.dto.LoginResponse;
import com.sopt.wokat.domain.member.dto.MemberProfileQueryDTO;
import com.sopt.wokat.domain.member.dto.OauthResponse;
import com.sopt.wokat.domain.member.dto.OauthTokenResponse;
import com.sopt.wokat.domain.member.entity.Member;
import com.sopt.wokat.domain.member.entity.ProfileImage;
import com.sopt.wokat.domain.member.exception.MemberNotFoundException;
import com.sopt.wokat.domain.member.oauth.OauthAttributes;
import com.sopt.wokat.domain.member.repository.MemberRepository;
import com.sopt.wokat.global.config.redis.RedisUtil;
import com.sopt.wokat.global.entity.Token;
import com.sopt.wokat.global.util.JwtTokenProvider;

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
    private final RedisUtil redisUtil;
    
    /**
     * @throws IOException
     * @InMemoryRepository application-oauth properties 정보를 담고 있음
     * @getToken() 넘겨받은 code 로 Oauth 서버에 Token 요청
     * @getUserProfile 첫 로그인 시 회원가입
     * 유저 인증 후 Jwt AccessToken, Refresh Token 생성
     * RefreshToken Redis 저장 만료기간 1달
     */
    
    @Transactional
    public LoginResponse login(AuthorizationRequest authorizationRequest) throws IOException {
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(authorizationRequest.getProviderName());
        Member member = getMemberProfile(authorizationRequest, provider);

        Token accessToken = jwtTokenProvider.createAccessToken(String.valueOf(member.getId()));
        Token refreshToken = jwtTokenProvider.createRefreshToken();

        redisUtil.setDataExpire(String.valueOf(member.getId()), refreshToken.getValue(), refreshToken.getExpiredTime());
        
        boolean validateMember = validateProfileSaveMember(member.getId());
        
        return LoginResponse.builder()
                .id(member.getId())
                .nickName(member.getMemberProfile().getNickName())
                .profileImage(member.getProfileImage().getS3URL())
                .userEmail(member.getMemberProfile().getUserEmail())
                .tokenType(AUTHORIZATION_TYPE)
                .accessToken(accessToken.getValue())
                .refreshToken(refreshToken.getValue())
                .profileSaveUser(validateMember)
                .build();
    }
    
    private Member getMemberProfile(AuthorizationRequest authorizationRequest, ClientRegistration provider) throws IOException {
        OauthTokenResponse token = getToken(authorizationRequest, provider);
        Map<String, Object> userAttributes = getUserAttributes(provider, token);
        
        OauthResponse extract = OauthAttributes.extract(authorizationRequest.getProviderName(), userAttributes);

        return saveOrUpdate(extract);
    }

    //! 저장, 변경 메소드 
    //! To-DO update
    private Member saveOrUpdate(OauthResponse member) throws IOException {
        Member findMember = memberRepository.findByOauthID(member.getMember().getMemberProfile().getProviderId());

        if (findMember == null) {
            ProfileImage profileImage = memberRepository.saveMemberProfileImage(member.getOauthURL());
            memberRepository.saveMemberProfile(member.getMember().getMemberProfile());
            
            //! 유저에 프로필 이미지 저장 
            member.getMember().setProfileImage(profileImage);
            findMember = memberRepository.save(member.getMember());
        }
        //! 저장된 oauthurl과 다르면 s3에 저장하고 업데이트
        else if (!findMember.getProfileImage().getOauthURL().equals(member.getOauthURL())) {
            //* 새로운 프로필 이미지 저장 
            ProfileImage profileImage = memberRepository.saveMemberProfileImage(member.getOauthURL());
            //* 유저 이미지 업데이트
            Member updatedMember = memberRepository.updateProfileImage(findMember, profileImage);
            memberRepository.save(updatedMember);
        }
        
        return findMember;
    }

    //* 카카오 서버로 토큰 요청 
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

    //* 카카오 서버에서 유저 정보 가져오기 
    private Map<String, Object> getUserAttributes(ClientRegistration provider, OauthTokenResponse tokenResponse) {
        return WebClient.create()
                    .get()
                    .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                    .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
    }

    private boolean validateProfileSaveMember(String id) {
        MemberProfileQueryDTO findMember = memberRepository.findMemberProfileById(id)
            .orElseThrow(() -> new MemberNotFoundException());

        if (findMember.getNickName() == null || 
            findMember.getProfileImage() == null ||
            findMember.getUserEmail() == null) {
            return false;
        } 
        return true;
    }

}
