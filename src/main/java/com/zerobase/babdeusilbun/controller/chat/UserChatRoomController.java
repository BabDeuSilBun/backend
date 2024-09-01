package com.zerobase.babdeusilbun.controller.chat;

import com.zerobase.babdeusilbun.dto.ChatDto.Information;
import com.zerobase.babdeusilbun.dto.ChatDto.RoomInformation;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.ChatService;
import com.zerobase.babdeusilbun.swagger.annotation.chat.UserChatRoomSwagger.GetChatMessagesOnChatRoomSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.chat.UserChatRoomSwagger.GetChatRoomsSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/chat-rooms")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserChatRoomController {
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
}
