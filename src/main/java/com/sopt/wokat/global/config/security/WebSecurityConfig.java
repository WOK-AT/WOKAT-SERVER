package com.sopt.wokat.global.config.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sopt.wokat.global.config.security.filter.JwtAuthenticationEntryPoint;
import com.sopt.wokat.global.config.security.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationConfiguration authenticationConfiguration;

    private static final String[] AUHT_WHITELIST_SWAGGER = {
        "/swagger-resources/**",
        "/swagger/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-ui/index.html",
        "/v3/api-docs/**"
    };

    private static final String[] AUTH_WHITELIST = {
        "/member/login/**"
    };

    @Bean
	public AuthenticationEntryPointFailureHandler authenticationEntryPointFailureHandler() {
		return new AuthenticationEntryPointFailureHandler(jwtAuthenticationEntryPoint);
	}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { 
        return (web) -> web
                            .ignoring()
                            .requestMatchers(AUHT_WHITELIST_SWAGGER);
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {

        http
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    
        //! rest-api에서 세션 기반 인증 대신 토큰 인증 사용 위해 로그아웃, 폼로그인, HTTP 기반 인증 기능 disable 
        http
            .logout(LogoutConfigurer<HttpSecurity>::disable)   
            .formLogin(AbstractAuthenticationFilterConfigurer::disable)
            .httpBasic(HttpBasicConfigurer<HttpSecurity>::disable);
        
        http
            .csrf(AbstractHttpConfigurer::disable)  //! CSRF(Cross-Site Request Forgery) 공격 방지 기능 비활성화
            .cors(cors -> cors.configurationSource(configurationSource()))  //! cors 설정 
            .authorizeHttpRequests(authorize -> {
                authorize
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll() 
                        .anyRequest().authenticated();  //! 모든 요청 인증 필요함 
            });

        http
            .exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthenticationEntryPoint));
            
        http  
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); //! TO-DO 클라이언트 도메인 추가하기 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        //configuration.setExposedHeaders(Arrays.asList("Content-Type", "Authorization"));

        configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);  //! CORS preflight 요청 1시간으로 설정 
        

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
    }

}
