package com.sopt.wokat.infra.tmap.WalkingDistance;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
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
public class APIGetWalkingDist {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    
    @Value("${tmap.client-id}")
    private String TMAP_API_KEY;

    public int getWalkingDistance(CoordinateDTO departure, String departName, CoordinateDTO destination, String destName) throws URISyntaxException, JSONException {
        int totalTime = Integer.MAX_VALUE;

        RestTemplate restTemplate = new RestTemplate();

        //! 요청 URL, 토큰 설정
        String url = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1";

        //! 헤더에 Authorization 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("appKey", TMAP_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        //! 바디에 정보 추가 
        JSONObject requestBody = new JSONObject();
        requestBody.put("startX", departure.getLongitude());
        requestBody.put("startY", departure.getLatitude());
        requestBody.put("endX", destination.getLongitude());
        requestBody.put("endY", destination.getLatitude());
        requestBody.put("startName", departName);
        requestBody.put("endName", destName);
        requestBody.put("searchOption", 10);

        HttpEntity<?> requestEntity = new HttpEntity<>(requestBody.toString(),headers);

        //! POST 요청 
        ResponseEntity<ResponseBody> response = restTemplate.exchange( 
            url, HttpMethod.POST, requestEntity, ResponseBody.class);

        //! 응답 처리
        if (response.getStatusCode().is2xxSuccessful()) {
            ResponseBody responseBody = response.getBody();
            if (responseBody != null) {
                Features[] features = responseBody.getFeatures();
                if (features != null && features.length > 0) {
                    Properties properties = features[0].getProperties();
                    if (properties != null) {
                        totalTime = properties.getTotalTime();
                    }
                } else {
                    LOGGER.info("No documents found.");
                }
            } else {
                LOGGER.info("Response body is empty.");
            }
        } else {
            LOGGER.error("Request failed with status code: " + response.getStatusCode());
        }
        return totalTime;
    }

}
