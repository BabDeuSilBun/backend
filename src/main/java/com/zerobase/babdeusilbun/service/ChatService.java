package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.ChatDto.Request;
import com.zerobase.babdeusilbun.dto.ChatDto.Information;
import com.zerobase.babdeusilbun.dto.ChatDto.RoomInformation;
import org.springframework.data.domain.Page;

public interface ChatService {
  void enteredChatRoom(User user, Meeting meeting);
  Page<RoomInformation> getChatRooms(Long userId, int page, int size);
  Page<Information> getChatMessagesOnChatRoom(Long userId, Long chatRoomId, int page, int size);
  Information sendMessage(Long chatRoomId, Long userId, Request request);
  Information leaveChatRoomForChatRoomIdAndUserId(Long chatRoomId, Long userId);
}
