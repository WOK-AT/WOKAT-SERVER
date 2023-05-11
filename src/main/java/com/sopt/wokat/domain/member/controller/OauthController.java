package com.sopt.wokat.domain.member.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sopt.wokat.domain.member.dto.LoginResponse;
import com.sopt.wokat.domain.member.service.OauthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/member/login")
@Tag(name = "Kakao", description = "간편로그인") 
public class OauthController {

    private final OauthService oauthService;
    private final Logger LOGGER = LogManager.getLogger(this.getClass());
    
    @GetMapping(value="/{provider}")
    @Operation(summary = "카카오", description = "카카오 회원가입 및 간편로그인을 진행합닌다.", tags = {"Kakao"})
    public ResponseEntity<LoginResponse> kakaoLogin(@PathVariable String provider, @RequestParam String code) {
        LOGGER.info(code);
        LoginResponse loginResponse = oauthService.login(provider, code);

        return ResponseEntity.ok().body(loginResponse);
    }

}