package com.sopt.wokat.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.context.annotation.*;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "✨WOKAT Swagger✨",
                version = "v1.0.0",
                description = "SOPT - WOKAT SERVER ",
                contact = @Contact(
                        name = "WOKAT Github",
                        url = "https://github.com/Maro-SOPT/server"
                )
        ),
        servers = @Server(url = "/")   //스웨거 테스트 시 http → https로 요청하기 위해
)
public class SwaggerConfig {

        public OpenAPI openAPI() {

                return new OpenAPI();
                        
        }

}