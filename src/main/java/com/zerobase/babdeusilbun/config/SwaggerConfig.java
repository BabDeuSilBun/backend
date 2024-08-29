package com.zerobase.babdeusilbun.config;

import com.zerobase.babdeusilbun.enums.SwaggerTag;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(servers = {
    @io.swagger.v3.oas.annotations.servers.Server(url = "/", description = "Default Server URL")
})
@Configuration
public class SwaggerConfig {
  private final String accessTokenKey = "Access Token(Bearer)";
  private final Server localHttpServer = new Server().url("http://localhost:8080").description("request to http localhost");
  private final Server localHttpsServer = new Server().url("https://localhost:8080").description("request to https localhost");
//  private final Server deployedHttpsServer = new Server().url("https://babdeusilbun.kro.kr").description("request to deployed server");

  private final List<Tag> entrepreneurTag = List.of(
      SwaggerTag.AUTHORIZATION_ENTREPRENEUR.getTag()
  );
  private final List<Tag> userTag = List.of(
      SwaggerTag.AUTHORIZATION_USER.getTag()
  );
  private final List<Tag> commonTag = List.of(
      SwaggerTag.COMMON_SCHOOL.getTag()
  );

  @Bean
  public GroupedOpenApi entrepreneurs() {
    return GroupedOpenApi.builder()
        .group("Entrepreneur")
        .pathsToMatch("/entrepreneur/**")
        .addOpenApiCustomizer(openApi -> {
          openApi.addSecurityItem(securityRequirementList())
              .setTags(entrepreneurTag);
        })
        .build();
  }

  @Bean
  public GroupedOpenApi user() {
    return GroupedOpenApi.builder()
        .group("User")
        .pathsToMatch("/user/**")
        .addOpenApiCustomizer(openApi -> {
          openApi.addSecurityItem(securityRequirementList())
              .setTags(userTag);
        })
        .build();
  }

  @Bean
  public GroupedOpenApi common() {
    return GroupedOpenApi.builder()
        .group("Common")
        .pathsToMatch("/common/**")
        .addOpenApiCustomizer(openApi -> {
          openApi.setTags(commonTag);
        })
        .build();
  }

  @Bean
  public GroupedOpenApi all() {
    return GroupedOpenApi.builder()
        .group("All")
        .addOpenApiCustomizer(openApi -> {
          openApi.addSecurityItem(securityRequirementList());
        })
        .build();
  }

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
//        .servers(List.of(deployedHttpsServer, localHttpsServer, localHttpServer))
        .servers(List.of(localHttpsServer, localHttpServer))
        .info(apiInfo())
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