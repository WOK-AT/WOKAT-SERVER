package com.sopt.wokat.infra.kakao.LocationToCoord;

import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sopt.wokat.domain.place.dto.CoordinateDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class APILocationToCoord {
    
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String KAKAO_API_KEY;

    public CoordinateDTO getCoordByLocation(String location) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        CoordinateDTO coordinate = new CoordinateDTO();

        //! 요청 URL, 토큰 설정
        String url = String.format("https://dapi.kakao.com/v2/local/search/address.json?query=%s", 
                String.valueOf(location));

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
                    coordinate = new CoordinateDTO(
                        documents[0].getLongitude(),
                        documents[0].getLatitude()
                    );
                    return coordinate;
                } else {
                    LOGGER.info("No documents found.");
                }
            } else {
                LOGGER.info("Response body is empty.");
            }
        } else {
            LOGGER.error("Request failed with status code: " + response.getStatusCode());
        }
        return coordinate;
    }
}
