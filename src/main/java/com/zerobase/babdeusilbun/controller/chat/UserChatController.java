package com.zerobase.babdeusilbun.controller.chat;

import static com.zerobase.babdeusilbun.dto.ChatDto.Request;

import com.zerobase.babdeusilbun.dto.ChatDto.Information;
import com.zerobase.babdeusilbun.dto.ChatDto.RoomInformation;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.ChatService;
import com.zerobase.babdeusilbun.swagger.annotation.chat.UserChatSwagger.GetChatMessagesOnChatRoomSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.chat.UserChatSwagger.GetChatRoomsSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.chat.UserChatSwagger.LeaveFromChatRoomSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.chat.UserChatSwagger.SendChatMessageToChatRoomSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/chat-rooms")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserChatController {
  private final ChatService chatService;

  @GetMapping
  @GetChatRoomsSwagger
  public ResponseEntity<Page<RoomInformation>> getChatRooms(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {

    return ResponseEntity.ok(chatService.getChatRooms(user.getId(), page, size));
  }

  @GetMapping("/{chatRoomId}")
  @GetChatMessagesOnChatRoomSwagger
  public ResponseEntity<Page<Information>> getChatMessagesOnChatRoom(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("chatRoomId") Long chatRoomId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {

    return ResponseEntity.ok(chatService.getChatMessagesOnChatRoom(user.getId(), chatRoomId, page, size));
  }

  @PostMapping("/{chatRoomId}")
  @SendChatMessageToChatRoomSwagger
  public ResponseEntity<Void> sendChatMessageToChatRoom(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("chatRoomId") Long chatRoomId,
      @RequestBody Request request) {

    chatService.sendMessage(chatRoomId, user.getId(), request);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/{chatRoomId}/leave")
  @LeaveFromChatRoomSwagger
  public ResponseEntity<Void> leaveFromChatRoom(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("chatRoomId") Long chatRoomId) {

    chatService.leaveChatRoomForChatRoomIdAndUserId(chatRoomId, user.getId());

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
