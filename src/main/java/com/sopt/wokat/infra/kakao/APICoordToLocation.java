package com.sopt.wokat.infra.kakao;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String KAKAO_API_KEY;
    
    public String getAreaByCoordinates(String longitude, String latitude) {
        String responseBody = "";

        try {
            String url = String.format("https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=%s&y=%s", 
                    String.valueOf(longitude), String.valueOf(latitude));

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + KAKAO_API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, new URI(url));

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                responseBody = response.getBody();
                System.out.println(responseBody);
            } else {
                System.out.println("Request failed with status code: " + response.getStatusCode());
            }

            return responseBody;
            
        } catch (URISyntaxException e) {
            throw new KakaoAPIRequestException(ErrorCode.COORDS_TO_LOCATION_FAIL);
        }
    }
}
