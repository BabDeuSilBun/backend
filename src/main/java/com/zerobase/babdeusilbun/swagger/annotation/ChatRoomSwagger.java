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

public @interface ChatRoomSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "채팅방 목록 조회", description = "(최신 메세지 등록순 정렬)")
  @Parameters(value = {
      @Parameter(name = "user"),
      @Parameter(name = "page"),
      @Parameter(name = "size")
  })
  @Tag(name = "Chat")
  @interface GetChatRoomsSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "채팅방 메세지 조회", description = "(최신순 정렬)")
  @Parameters(value = {
      @Parameter(name = "user"),
      @Parameter(name = "chatRoomId"),
      @Parameter(name = "page"),
      @Parameter(name = "size")
  })
  @Tag(name = "Chat")
  @interface GetChatMessagesOnChatRoomSwagger {}

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "채팅방 퇴장")
  @Parameters(value = {
      @Parameter(name = "user"),
      @Parameter(name = "chatRoomId"),
  })
  @Tag(name = "Chat")
  @interface LeaveOnChatRoomSwagger {}

}
