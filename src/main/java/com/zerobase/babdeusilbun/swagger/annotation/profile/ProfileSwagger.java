package com.zerobase.babdeusilbun.swagger.annotation.profile;

import com.zerobase.babdeusilbun.dto.UserDto.Profile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface ProfileSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "프로필 조회 api",
      description = "일반 이용자의 공개 정보 조회")
  @Parameter(name = "userId", description = "조회하려는 일반 이용자의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "프로필 조회에 성공한 경우",
          content = @Content(schema = @Schema(implementation = Profile.class)))
  })
  @Tag(name = "Entrepreneur Profile Api")
  @Tag(name = "User Profile Api")
  @interface GetUserProfileSwagger {}
}
