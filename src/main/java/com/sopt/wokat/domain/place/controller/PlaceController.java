package com.sopt.wokat.domain.place.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sopt.wokat.domain.place.dto.PostPlaceRequest;
import com.sopt.wokat.domain.place.service.PlaceService;
import com.sopt.wokat.global.result.ResultCode;
import com.sopt.wokat.global.result.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
@Tag(name = "Place", description = "공간 관련 API") 
public class PlaceController {
    
    private final PlaceService placeService;

    @Operation(
        summary = "장소 필터링", 
        description = "위치를 이용해 장소를 필터링하는 API입니다.", 
        tags = {"Place"},
        parameters = {
            @Parameter(name = "station", description = "역 이름", in = ParameterIn.QUERY, example = "안국역"),
            @Parameter(name = "filter", description = "거리순/북마크순 필터링", in = ParameterIn.QUERY, example = "stars(북마크순)/distance(거리순)"),
            @Parameter(name = "date", description = "예약 날짜", in = ParameterIn.QUERY, example = "2023-05-18"),
            @Parameter(name = "person", description = "예약 인원수", in = ParameterIn.QUERY, example = "2")
        }
    )
    @GetMapping(value = "")
    public ResponseEntity<ResultResponse> getFilteredPlace(
        @RequestParam(required = true) String station,
        @RequestParam(required = true) String filter,
        @RequestParam(required = false) String date,
        @RequestParam(required = false) String person
    ) {
        ResultResponse response;
        try {
            response = ResultResponse.of(ResultCode.LOGIN_SUCCESS,
                    placeService.filteringPlace());
        } catch (Exception e){
            response = ResultResponse.of(ResultCode.LOGIN_FAIL, e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @Operation(
        summary = "장소 등록", 
        description = "장소를 등록하는 API입니다.", 
        tags = {"Place"}
    )
    @PostMapping(
        value = "", 
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResultResponse> postPlace(
        @Parameter(description = "등록할 공간 사진 배열")
        @RequestPart(name = "multipartFile", required = false) List<MultipartFile> multipartFile,
        @Parameter(description = "등록할 공간 정보", name = "placeRequest", required = true)
        @RequestBody PostPlaceRequest placeRequest
    ) {
        ResultResponse response;
        try {
            response = ResultResponse.of(ResultCode.POST_PLACE_SUCCESS,
                    placeService.postPlace(multipartFile, placeRequest));
        } catch (Exception e){
            response = ResultResponse.of(ResultCode.POST_PLACE_FAIL, e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @Operation(
        summary = "장소 정보 반환", 
        description = "특정 장소의 정보를 반환하는 API입니다.", 
        tags = {"Place"},
        parameters = {
            @Parameter(name = "placeList", description = "공간 카테고리", in = ParameterIn.PATH),
            @Parameter(name = "placeId", description = "공간 ID", in = ParameterIn.PATH)
        }
    )
    @GetMapping(value = "/{placeId}")
    public ResponseEntity<ResultResponse> getOnePlace(
        @PathVariable("placeId") String placeId
    ) {
        ResultResponse response;
        try {
            response = ResultResponse.of(ResultCode.GET_PLACE_SUCCESS,
                    placeService.findPlaceInfo(placeId));
        } catch (Exception e){
            response = ResultResponse.of(ResultCode.GET_PLACE_FAIL, e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @Operation(
        summary = "주소 변환", 
        description = "도로명주소와 지번주소를 서로 변환하는 API입니다.", 
        tags = {"Place"},
        parameters = {
            @Parameter(name = "placeId", description = "공간 ID", in = ParameterIn.PATH),
            @Parameter(name = "isRoadName", description = "도로명주소(true)/지번주소(false)", in = ParameterIn.PATH)
        }
    )
    @GetMapping(value = "/{placeId}/address/{isRoadName}")
    public ResponseEntity<ResultResponse> getPlaceLocation(
        @PathVariable("placeId") String placeId,
        @PathVariable("isRoadName") int isRoadName)
    {

        ResultResponse response;
        try {
            response = ResultResponse.of(ResultCode.GET_PLACE_ADDRESS_SUCCESS,
                    placeService.findPlaceLocation(placeId, isRoadName));
        } catch (Exception e){
            response = ResultResponse.of(ResultCode.GET_PLACE_ADDRESS_FAIL, e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

}
