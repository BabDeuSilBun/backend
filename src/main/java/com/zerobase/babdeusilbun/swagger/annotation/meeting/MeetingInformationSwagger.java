package com.zerobase.babdeusilbun.swagger.annotation.meeting;

import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.dto.MeetingHeadCountDto;
import com.zerobase.babdeusilbun.dto.MeetingUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

public @interface MeetingInformationSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임 정보 조회 api",
      description = "모임의 정보를 조회")
  @Parameter(name = "meetingId", description = "조회하려는 모임의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "모임 정보 조회에 성공한 경우",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = MeetingDto.class)))
  })
  @Tag(name = "User Meeting Information Api")
  @Tag(name = "Entrepreneur Meeting Information Api")
  @interface GetMeetingInfoSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임장 조회 api",
      description = "모임의 모임장을 조회")
  @Parameter(name = "meetingId", description = "조회하려는 모임의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "모임장 조회에 성공한 경우",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = MeetingUserDto.class)))
  })
  @Tag(name = "User Meeting Information Api")
  @Tag(name = "Entrepreneur Meeting Information Api")
  @interface GetMeetingLeaderInfoSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임원 조회 api",
      description = "모임에 참여한 모임원 목록 조회")
  @Parameters(value = {
      @Parameter(name = "meetingId", description = "조회하려는 모임의 id", in = ParameterIn.PATH),
      @Parameter(name = "pageable", description = "모임원 정보 목록에서 보일 페이지번호와 한 페이지당 보이는 항목 개수")
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "모임에 참여한 모임원 목록 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = MeetingUserDto.class))))
  })
  @Tag(name = "User Meeting Information Api")
  @Tag(name = "Entrepreneur Meeting Information Api")
  @interface GetMeetingParticipantInfoSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "모임 참여 인원 수 조회 api",
      description = "모임에 참여한 인원 수를 조회")
  @Parameter(name = "meetingId", description = "조회하려는 모임의 id", in = ParameterIn.PATH)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "모임에 참여한 인원 수 조회에 성공한 경우",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = MeetingHeadCountDto.class)))
  })
  @Tag(name = "User Meeting Information Api")
  @Tag(name = "Entrepreneur Meeting Information Api")
  @interface GetMeetingHeadCount {}
}
