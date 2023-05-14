package com.sopt.wokat.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JwtDTO {
    
    private String type;
	private String accessToken;
	private String refreshToken;
    
}
