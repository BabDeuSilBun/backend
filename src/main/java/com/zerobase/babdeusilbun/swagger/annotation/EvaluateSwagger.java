package com.zerobase.babdeusilbun.swagger.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface EvaluateSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "받은 평가 내역 조회")
  @Tag(name = "Evaluate")
  @interface GetMyEvaluatesSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "모임원 평가")
  @Parameters(value = {
      @Parameter(name = "meetingId"),
      @Parameter(name = "participantId"),
  })
  @Tag(name = "Evaluate")
  @interface EvaluateParticipantSwagger {}

}
