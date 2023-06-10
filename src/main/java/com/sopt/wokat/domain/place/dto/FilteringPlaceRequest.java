package com.sopt.wokat.domain.place.dto;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilteringPlaceRequest {
    
    @Parameter(name = "station", description = "역 이름", required = true,  in = ParameterIn.QUERY, example = "안국역")
    private String station;

    @Parameter(name = "filter", description = "거리순/북마크순 필터링", required = true,  in = ParameterIn.QUERY, example = "1(북마크순)/0(거리순)")
    private Integer filter;

    /*
     * 
    @Parameter(name = "date", description = "예약 날짜", required = false, in = ParameterIn.QUERY, example = "2023-05-18-목")
    private String date;

    @Parameter(name = "headCount", description = "예약 인원수", required = false, in = ParameterIn.QUERY, example = "5")
    private Integer headCount;
     */

    @Parameter(name = "page", description = "페이지네이션", required = true, in = ParameterIn.QUERY, example = "2")
    private Integer page;

}