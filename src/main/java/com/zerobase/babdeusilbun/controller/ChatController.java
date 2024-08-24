package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.dto.ChatDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
  private final ChatService chatService;

  @PreAuthorize("hasRole('USER')")
  @MessageMapping("/sendMessage/{chatRoomId}")
  public void sendMessage(
      @DestinationVariable("chatRoomId") Long chatRoomId,
      @AuthenticationPrincipal CustomUserDetails user, ChatDto.Request request
  ) {
    chatService.sendMessage(chatRoomId, user.getId(), request);
  }

  @PreAuthorize("hasRole('USER')")
  @MessageMapping("/leaveRoom/{chatRoomId}")
  public void leaveRoom(
      @DestinationVariable("chatRoomId") Long chatRoomId, @AuthenticationPrincipal CustomUserDetails user) {
    chatService.leaveChatRoomForChatRoomIdAndUserId(chatRoomId, user.getId());
  }
}
