package com.zerobase.babdeusilbun.websocket.controller;

import com.zerobase.babdeusilbun.dto.ChatDto;
import com.zerobase.babdeusilbun.dto.ChatDto.Information;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserChatController {
  private final ChatService chatService;

  @MessageMapping("/chat-rooms/{chatRoomId}")
  @SendTo("/meeting/chat-rooms/{chatRoomId}")
  public Information sendChatMessageToChatRoom(
      CustomUserDetails user,
      @DestinationVariable("chatRoomId") Long chatRoomId,
      @Payload ChatDto.Request request) {

    return chatService.sendMessage(chatRoomId, user.getId(), request);
  }

  @MessageMapping("/chat-rooms/{chatRoomId}/leave")
  @SendTo("/meeting/chat-rooms/{chatRoomId}")
  public Information leaveFromChatRoom(
      CustomUserDetails user,
      @DestinationVariable("chatRoomId") Long chatRoomId) {

    return chatService.leaveChatRoomForChatRoomIdAndUserId(chatRoomId, user.getId());
  }
}
