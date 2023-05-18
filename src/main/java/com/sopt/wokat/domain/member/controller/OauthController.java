package com.sopt.wokat.domain.member.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopt.wokat.domain.member.dto.AuthorizationRequest;
import com.sopt.wokat.domain.member.dto.LoginResponse;
import com.sopt.wokat.domain.member.service.OauthService;
import com.sopt.wokat.global.result.ResultCode;
import com.sopt.wokat.global.result.ResultResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/member/login")
@RequiredArgsConstructor
@Tag(name = "Kakao", description = "카카오 간편로그인") 
public class OauthController {

    private final OauthService oauthService;
    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    
    @Operation(summary = "소셜 로그인", description = "카카오 회원가입 및 간편로그인을 진행합닌다.", tags = {"Kakao"})
    @GetMapping(value="/{provider}")
    public ResponseEntity<ResultResponse> kakaoLogin(@PathVariable String provider, 
                                                        @RequestParam String code) throws IOException {
        ResultResponse response;

        response = ResultResponse.of(ResultCode.LOGIN_SUCCESS, 
                oauthService.login(new AuthorizationRequest(provider, code)));
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
    

}