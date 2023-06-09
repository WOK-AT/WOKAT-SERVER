package com.sopt.wokat.infra.kakao;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sopt.wokat.global.error.ErrorCode;
import com.sopt.wokat.global.error.exception.KakaoAPIRequestException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class APICoordToLocation {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String KAKAO_API_KEY;
    
    public String getAreaByCoordinates(String longitude, String latitude) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        String addressName = "";

        //! 요청 URL, 토큰 설정
        String url = String.format("https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=%s&y=%s", 
                String.valueOf(longitude), String.valueOf(latitude));

        //! 헤더에 Authorization 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + KAKAO_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        //! GET 요청 
        ResponseEntity<APIResponseBody> response = restTemplate.exchange( 
            url, HttpMethod.GET, requestEntity, APIResponseBody.class);

        //! 응답 처리

        if (response.getStatusCode().is2xxSuccessful()) {
            APIResponseBody apiResponseBody = response.getBody();
            if (apiResponseBody != null) {
                APIDocument[] documents = apiResponseBody.getDocuments();
                if (documents != null && documents.length > 0) {
                    addressName = documents[0].getAddressName();
                    LOGGER.info("Address Name: " + addressName);
                } else {
                    LOGGER.info("No documents found.");
                }
            } else {
                LOGGER.info("Response body is empty.");
            }
        } else {
            LOGGER.error("Request failed with status code: " + response.getStatusCode());
        }
        
        return addressName;
    }
}
