package com.zerobase.babdeusilbun.swagger.annotation.chat;

import com.zerobase.babdeusilbun.dto.ChatDto.Information;
import com.zerobase.babdeusilbun.dto.ChatDto.Request;
import com.zerobase.babdeusilbun.dto.ChatDto.RoomInformation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface UserChatSwagger {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "입장 가능한 채팅방 목록 조회 api",
      description = "입장 가능한 채팅방 목록을 가장 최근에 메세지가 온 방 순서대로 정렬")
  @Parameters(value = {
      @Parameter(name = "page", description = "조회하려는 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "조회하려는 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "채팅방 목록 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = RoomInformation.class))))
  })
  @Tag(name = "User Chat Api")
  @interface GetChatRoomsSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "채팅방 메세지 조회 api",
      description = "입장한 채팅방의 채팅 내역을 최신순으로 정렬")
  @Parameters(value = {
      @Parameter(name = "chatRoomId", description = "채팅 내역을 조회하려는 채팅방의 id", in = ParameterIn.PATH),
      @Parameter(name = "page", description = "조회하려는 페이지 번호(시작점: 0, 기본값: 0)", in = ParameterIn.QUERY),
      @Parameter(name = "size", description = "조회하려는 페이지의 요소 개수(최소:1, 기본값: 10)", in = ParameterIn.QUERY)
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "채팅방 메세지 조회에 성공한 경우",
          content = @Content(mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = Information.class))))
  })
  @Tag(name = "User Chat Api")
  @interface GetChatMessagesOnChatRoomSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(
      summary = "채팅 전송 api",
      description = "채팅방으로 채팅 전송")
  @Parameters(value = {
      @Parameter(name = "chatRoomId", description = "메세지를 전송하려는 채팅방의 id", in = ParameterIn.PATH),
  })
  @RequestBody(
      content = @Content(schema = @Schema(implementation = Request.class)),
      description = "보낼 메세지 내용")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "메세지 전송에 성공한 경우")
  })
  @Tag(name = "User Chat Api")
  @interface SendChatMessageToChatRoomSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "채팅방 퇴장 api")
  @Parameters(value = {
      @Parameter(name = "chatRoomId", description = "퇴장하려는 채팅방의 id", in = ParameterIn.PATH),
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "퇴장에 성공한 경우")
  })
  @Tag(name = "User Chat Api")
  @interface LeaveFromChatRoomSwagger {}
}
