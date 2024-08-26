package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.dto.ChatDto;
import com.zerobase.babdeusilbun.dto.ChatDto.Information;
import com.zerobase.babdeusilbun.dto.ChatDto.RoomInformation;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.ChatService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRoomController {
  private final ChatService chatService;

  /**
   * 채팅방 목록 가져오는 api (최신 메세지 등록순 정렬)
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/users/chat-rooms")
  public ResponseEntity<Page<RoomInformation>> getChatRooms(
      @AuthenticationPrincipal CustomUserDetails user,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(chatService.getChatRooms(user.getId(), page, size));
  }

  /**
   * 채팅방 메세지 가져오는 api (최신순 정렬)
   */
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/users/chat-rooms/{chatRoomId}")
  public ResponseEntity<Page<Information>> getChatMessagesOnChatRoom(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("chatRoomId") Long chatRoomId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(chatService.getChatMessagesOnChatRoom(user.getId(), chatRoomId, page, size));
  }

  /**
   * 채팅 전송 api
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/users/chat-rooms/{chatRoomId}")
  public ResponseEntity<Void> getChatMessagesOnChatRoom(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("chatRoomId") Long chatRoomId,
      @RequestBody ChatDto.Request request) {
    chatService.sendMessage(chatRoomId, user.getId(), request);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 채팅방 퇴장 api
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/users/chat-rooms/{chatRoomId}/leave")
  public ResponseEntity<Void> leaveOnChatRoom(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable("chatRoomId") Long chatRoomId) {
    chatService.leaveChatRoomForChatRoomIdAndUserId(chatRoomId, user.getId());

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
