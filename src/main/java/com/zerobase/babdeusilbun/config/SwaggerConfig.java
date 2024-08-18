package com.zerobase.babdeusilbun.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  private final String accessTokenKey = "Access Token(Bearer)";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(apiInfo())
        .addSecurityItem(securityRequirementList())
        .components(components());
  }

  private Info apiInfo() {
    return new Info()
        .title("밥드실분 API 명세서")
        .description("같은 대학의 학생 및 교직원들이 교내 혹은 기숙사에서 "
            + "배달 음식을 함께 주문하고 수령할 수 있도록 모임을 주선하는 서비스")
        .version("1.0.0");
  }

  private SecurityRequirement securityRequirementList() {
    return new SecurityRequirement()
        .addList(this.accessTokenKey);
  }

  private SecurityScheme accessTokenSecurityScheme() {
    return new SecurityScheme()
        .type(Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .in(In.HEADER)
        .name("AccessToken");
  }

  private Components components() {
    return new Components()
        .addSecuritySchemes(this.accessTokenKey, accessTokenSecurityScheme());
  }
}