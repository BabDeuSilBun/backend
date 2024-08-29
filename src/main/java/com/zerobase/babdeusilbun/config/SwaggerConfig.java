package com.zerobase.babdeusilbun.config;

import static com.zerobase.babdeusilbun.swagger.enums.SwaggerServer.DEPLOYED_HTTPS_SERVER;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerServer.LOCAL_HTTPS_SERVER;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerServer.LOCAL_HTTP_SERVER;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.COMMON_LOOkUP;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.COMMON_SIGN;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.COMMON_STORE_INFORMATION;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.ENTREPRENEUR_AUTH;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.ENTREPRENEUR_LOOKUP;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.ENTREPRENEUR_MEETING_INFORMATION;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.ENTREPRENEUR_MENU;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.ENTREPRENEUR_PROFILE;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.ENTREPRENEUR_PURCHASE;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.ENTREPRENEUR_SIGN;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.ENTREPRENEUR_STORE_INFORMATION;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.ENTREPRENEUR_STORE_MANAGEMENT;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_AUTH;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_CHAT;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_INDIVIDUAL_CART;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_INQUIRY;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_LOOKUP;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_MEETING_INFORMATION;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_MEETING_MANAGEMENT;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_PAYMENT;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_POINT;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_PROFILE;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_PURCHASE;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_SIGN;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_STORE_INFORMATION;
import static com.zerobase.babdeusilbun.swagger.enums.SwaggerTag.USER_TEAM_CART;

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

@Configuration
public class SwaggerConfig {
  public static String ACCESS_TOKEN_KEY = "Access Token(Bearer)";
  public static List<Server> OPENAPI_SERVER = List.of(
      DEPLOYED_HTTPS_SERVER.getServer(), LOCAL_HTTPS_SERVER.getServer(), LOCAL_HTTP_SERVER.getServer()
  );

  public static List<String> USER_TAG = List.of(
      USER_AUTH.getName(), USER_LOOKUP.getName(), USER_SIGN.getName(), USER_PROFILE.getName(),
      USER_CHAT.getName(), USER_TEAM_CART.getName(), USER_INDIVIDUAL_CART.getName(),
      USER_STORE_INFORMATION.getName(),
      USER_MEETING_MANAGEMENT.getName(), USER_MEETING_INFORMATION.getName(),
      USER_PURCHASE.getName(),
      USER_PAYMENT.getName(), USER_POINT.getName(),
      USER_INQUIRY.getName()
  );
  public static List<String> ENTREPRENEUR_TAG = List.of(
      ENTREPRENEUR_AUTH.getName(), ENTREPRENEUR_LOOKUP.getName(), ENTREPRENEUR_SIGN.getName(), ENTREPRENEUR_PROFILE.getName(),
      ENTREPRENEUR_MEETING_INFORMATION.getName(),
      ENTREPRENEUR_STORE_MANAGEMENT.getName(), ENTREPRENEUR_STORE_INFORMATION.getName(),
      ENTREPRENEUR_MENU.getName(),
      ENTREPRENEUR_PURCHASE.getName()
  );
  public static List<String> COMMON_TAG = List.of(
      COMMON_LOOkUP.getName(), COMMON_SIGN.getName(), COMMON_STORE_INFORMATION.getName()
  );

  public static List<Tag> SETTING_TAGS = List.of(
      USER_AUTH.getTag(), ENTREPRENEUR_AUTH.getTag(), USER_PROFILE.getTag(), ENTREPRENEUR_PROFILE.getTag(),
      USER_SIGN.getTag(), ENTREPRENEUR_SIGN.getTag(), COMMON_SIGN.getTag(),
      COMMON_LOOkUP.getTag(), USER_LOOKUP.getTag(), ENTREPRENEUR_LOOKUP.getTag(),
      ENTREPRENEUR_STORE_MANAGEMENT.getTag(),
      USER_STORE_INFORMATION.getTag(), ENTREPRENEUR_STORE_INFORMATION.getTag(), COMMON_STORE_INFORMATION.getTag(),
      USER_MEETING_MANAGEMENT.getTag(), USER_MEETING_INFORMATION.getTag(), ENTREPRENEUR_MEETING_INFORMATION.getTag(),
      USER_CHAT.getTag(), USER_TEAM_CART.getTag(), USER_INDIVIDUAL_CART.getTag(),
      USER_PURCHASE.getTag(), USER_PAYMENT.getTag(), USER_POINT.getTag(),
      ENTREPRENEUR_MENU.getTag(),
      ENTREPRENEUR_PURCHASE.getTag(),
      USER_INQUIRY.getTag()
  );

  @Bean
  public GroupedOpenApi entrepreneurs() {
    return GroupedOpenApi.builder()
        .group("03. 사업가 이용 가능 API")
        .addOpenApiCustomizer(openApi -> filterPaths(openApi, ENTREPRENEUR_TAG))
        .build();
  }

  @Bean
  public GroupedOpenApi user() {
    return GroupedOpenApi.builder()
        .group("02. 일반 이용자 이용 가능 API")
        .addOpenApiCustomizer(openApi -> filterPaths(openApi, USER_TAG))
        .build();
  }

  @Bean
  public GroupedOpenApi common() {
    return GroupedOpenApi.builder()
        .group("01. 로그인 없이 이용 가능 API")
        .addOpenApiCustomizer(openApi -> filterPaths(openApi, COMMON_TAG))
        .build();
  }

  @Bean
  public GroupedOpenApi all() {
    return GroupedOpenApi.builder()
        .group("00. 전체 API")
        .addOpenApiCustomizer(openApi ->
            openApi.setTags(SETTING_TAGS))
        .build();
  }

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .servers(OPENAPI_SERVER)
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
        .addList(ACCESS_TOKEN_KEY);
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
        .addSecuritySchemes(ACCESS_TOKEN_KEY, accessTokenSecurityScheme());
  }

  private void filterPaths(OpenAPI openApi, List<String> tags) {
    openApi.getPaths().entrySet().removeIf(entry ->
        entry.getValue().readOperations().removeIf(operation -> {
          List<String> filteredTags = operation.getTags().stream()
              .filter(tags::contains).toList();

          operation.setTags(filteredTags);

          return filteredTags.isEmpty();
        })
    );

    List<String> usedTags = openApi.getPaths().values().stream()
        .flatMap(pathItem -> pathItem.readOperations().stream())
        .flatMap(operation -> operation.getTags().stream())
        .distinct().toList();

    List<Tag> existingTags = openApi.getTags() != null ? openApi.getTags() : List.of();

    openApi.setTags(
        existingTags.stream()
            .filter(tag -> usedTags.contains(tag.getName())).toList());
  }
}