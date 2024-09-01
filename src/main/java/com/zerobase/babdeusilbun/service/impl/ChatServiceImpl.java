package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.util.ChatUtility.CHAT_SEPARATOR;
import static com.zerobase.babdeusilbun.util.ChatUtility.SEND_TO_CLIENT_PREFIX;
import static com.zerobase.babdeusilbun.util.ChatUtility.makeSocketDestination;

import com.zerobase.babdeusilbun.domain.Chat;
import com.zerobase.babdeusilbun.domain.ChatRoom;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.ChatDto.Information;
import com.zerobase.babdeusilbun.dto.ChatDto.Request;
import com.zerobase.babdeusilbun.dto.ChatDto.RoomInformation;
import com.zerobase.babdeusilbun.enums.ChatType;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.ChatRepository;
import com.zerobase.babdeusilbun.repository.ChatRoomRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.ChatService;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
  private final ChatRoomRepository chatRoomRepository;
  private final ChatRepository chatRepository;
  private final UserRepository userRepository;

  private final SimpMessagingTemplate messagingTemplate;

  private record UserChatRoomData(User user, ChatRoom chatRoom) {}

  private UserChatRoomData getUserAndChatRoom(Long userId, Long chatRoomId) {
    User user = userRepository.findByIdAndDeletedAtIsNull(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

    return new UserChatRoomData(user, chatRoom);
  }

  private boolean cannotProcessedInChatRoomByChatRoomAndUser(ChatRoom chatRoom, User user) {
    //메세지가 없거나 가장 최근 메세지가 떠남인 경우 return true
    return chatRepository.findTopByChatRoomAndUserOrderByCreatedAtDesc(chatRoom, user)
        .map(chat -> chat.getType() == ChatType.LEAVE).orElse(true);

  }

  private boolean cannotNewEnteredChatRoom(ChatRoom chatRoom, User user) {
    return chatRepository.findTopByChatRoomAndUserOrderByCreatedAtDesc(chatRoom, user)
        .filter(chat -> chat.getType() == ChatType.LEAVE).isEmpty();
  }

  @Override
  @Transactional
  public void enteredChatRoom(User user, Meeting meeting) {
    ChatRoom chatRoom = chatRoomRepository.findByMeeting(meeting)
        .orElseGet(() -> createChatRoomForMeeting(meeting));

    if (cannotNewEnteredChatRoom(chatRoom, user)) {
      return;
    }

    Chat chat = chatRepository.save(Chat.builder()
        .chatRoom(chatRoom)
        .user(user)
        .type(ChatType.ENTER)
        .content(ChatType.ENTER.getComment(user))
        .build());

    messagingTemplate.convertAndSend(makeSocketDestination(
        SEND_TO_CLIENT_PREFIX, CHAT_SEPARATOR, chatRoom.getId()), Information.fromEntity(chat));
  }

  private ChatRoom createChatRoomForMeeting(Meeting meeting) {
    return chatRoomRepository.save(
        ChatRoom.builder()
            .meeting(meeting)
            .build()
    );
  }

  @Override
  @Transactional(readOnly = true)
  public Page<RoomInformation> getChatRooms(Long userId, int page, int size) {
    User user = userRepository.findByIdAndDeletedAtIsNull(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    int count = chatRoomRepository.countRoomInformationByUser(user);
    if (count == 0) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 0);
    }

    size = (size <= 0) ? count : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Pageable pageable = PageRequest.of(page, size, Sort.by(
        Order.desc("id").with(Sort.NullHandling.NATIVE)));

    return chatRoomRepository.findRoomInformationByUser(user, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Information> getChatMessagesOnChatRoom(Long userId, Long chatRoomId, int page, int size) {
    UserChatRoomData data = getUserAndChatRoom(userId, chatRoomId);

    if (cannotProcessedInChatRoomByChatRoomAndUser(data.chatRoom(), data.user())) {
      throw new CustomException(ErrorCode.CANNOT_PROCESS_IN_CHATROOM);
    }

    int count = chatRepository.countChatsInRoomAfterUserEntered(data.chatRoom(), data.user());
    if (count == 0) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 0);
    }

    size = (size <= 0) ? count : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Pageable pageable = PageRequest.of(page, size, Sort.by(Order.desc("id")));

    return chatRepository.findChatsInRoomAfterUserEntered(data.chatRoom(), data.user(), pageable)
        .map(Information::fromEntity);
  }

  @Override
  @Transactional
  public Information sendMessage(Long chatRoomId, Long userId, Request request) {
    UserChatRoomData data = getUserAndChatRoom(userId, chatRoomId);

    if (cannotProcessedInChatRoomByChatRoomAndUser(data.chatRoom(), data.user())) {
      throw new CustomException(ErrorCode.CANNOT_PROCESS_IN_CHATROOM);
    }

    Chat chat = chatRepository.save(Chat.builder()
        .chatRoom(data.chatRoom())
        .user(data.user())
        .type(ChatType.CHAT)
        .content(request.getContent())
        .build());

    return Information.fromEntity(chat);
  }

  @Override
  @Transactional
  public Information leaveChatRoomForChatRoomIdAndUserId(Long chatRoomId, Long userId) {
    UserChatRoomData data = getUserAndChatRoom(userId, chatRoomId);

    return leaveChatRoom(data.chatRoom(), data.user());
  }

  public Information leaveChatRoom(ChatRoom chatRoom, User user) {
    if (cannotProcessedInChatRoomByChatRoomAndUser(chatRoom, user)) {
      return null;
    }

    Chat chat = chatRepository.save(Chat.builder()
        .chatRoom(chatRoom)
        .user(user)
        .type(ChatType.LEAVE)
        .content(ChatType.LEAVE.getComment(user))
        .build());

    return Information.fromEntity(chat);
  }
}
