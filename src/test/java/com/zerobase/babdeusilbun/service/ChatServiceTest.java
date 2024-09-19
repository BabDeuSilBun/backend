package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.util.ChatUtility.CHAT_SEPARATOR;
import static com.zerobase.babdeusilbun.util.ChatUtility.SEND_TO_CLIENT_PREFIX;
import static com.zerobase.babdeusilbun.util.ChatUtility.makeSocketDestination;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.domain.Chat;
import com.zerobase.babdeusilbun.domain.ChatRoom;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.ChatDto.Information;
import com.zerobase.babdeusilbun.dto.ChatDto.Request;
import com.zerobase.babdeusilbun.enums.ChatType;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.ChatRepository;
import com.zerobase.babdeusilbun.repository.ChatRoomRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.impl.ChatServiceImpl;
import com.zerobase.babdeusilbun.util.TestUserUtility;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
  @Mock
  private ChatRoomRepository chatRoomRepository;

  @Mock
  private ChatRepository chatRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SimpMessagingTemplate messagingTemplate;

  @InjectMocks
  private ChatServiceImpl chatService;

  private final User testUser = TestUserUtility.getUser();
  private final ChatRoom testChatRoom = ChatRoom.builder().id(1L).build();

  @DisplayName("채팅방 입장 성공 테스트")
  @Test
  void enteredChatRoomSuccess() {
    // given
    Meeting meeting = Meeting.builder().id(1L).build();
    Chat chat = Chat.builder()
        .chatRoom(testChatRoom)
        .user(testUser)
        .type(ChatType.ENTER)
        .content(ChatType.ENTER.getComment(testUser))
        .build();

    when(chatRoomRepository.findByMeeting(eq(meeting))).thenReturn(Optional.of(testChatRoom));
    when(chatRepository.findTopByChatRoomAndUserOrderByCreatedAtDesc(eq(testChatRoom), eq(testUser)))
        .thenReturn(Optional.of(Chat.builder().type(ChatType.LEAVE).build()));

    ArgumentCaptor<Chat> chatCaptor = ArgumentCaptor.forClass(Chat.class);
    when(chatRepository.save(chatCaptor.capture())).thenReturn(chat);

    // when
    chatService.enteredChatRoom(testUser, meeting);

    // then
    verify(chatRepository, times(1)).save(chatCaptor.capture());
    Chat savedChat = chatCaptor.getValue();

    assertEquals(testChatRoom, savedChat.getChatRoom());
    assertEquals(testUser, savedChat.getUser());
    assertEquals(ChatType.ENTER, savedChat.getType());
    assertEquals(ChatType.ENTER.getComment(testUser), savedChat.getContent());

    verify(messagingTemplate, times(1)).convertAndSend(eq(makeSocketDestination(
        SEND_TO_CLIENT_PREFIX, CHAT_SEPARATOR, testChatRoom.getId())), eq(Information.fromEntity(savedChat)));
  }

  @DisplayName("채팅방 메시지 가져오기 성공 테스트")
  @Test
  void getChatMessagesOnChatRoomSuccess() {
    // given
    Chat chat1 = Chat.builder()
        .id(1L)
        .chatRoom(testChatRoom)
        .user(testUser)
        .type(ChatType.CHAT)
        .content("1번메세지")
        .build();

    Chat chat2 = Chat.builder()
        .id(2L)
        .chatRoom(testChatRoom)
        .user(testUser)
        .type(ChatType.CHAT)
        .content("2번메세지")
        .build();

    List<Chat> chatList = List.of(chat1, chat2);
    Page<Chat> chatPage = new PageImpl<>(chatList, PageRequest.of(0, 10), chatList.size());

    when(userRepository.findByIdAndDeletedAtIsNull(eq(testUser.getId()))).thenReturn(Optional.of(testUser));
    when(chatRoomRepository.findById(eq(testChatRoom.getId()))).thenReturn(Optional.of(testChatRoom));
    when(chatRepository.findTopByChatRoomAndUserOrderByCreatedAtDesc(eq(testChatRoom), eq(testUser)))
        .thenReturn(Optional.of(Chat.builder().type(ChatType.CHAT).build()));
    when(chatRepository.countChatsInRoomAfterUserEntered(eq(testChatRoom), eq(testUser))).thenReturn(chatList.size());
    when(chatRepository.findChatsInRoomAfterUserEntered(eq(testChatRoom), eq(testUser),
        eq(PageRequest.of(0, 10, Sort.by(Order.desc("id"))))))
        .thenReturn(chatPage);

    // when
    Page<Information> result = chatService.getChatMessagesOnChatRoom(testUser.getId(), testChatRoom.getId(), 0, 10);

    // then
    assertEquals(2, result.getTotalElements());

    // Information 객체의 필드 검증
    assertEquals("1번메세지", result.getContent().get(0).getContent());
    assertEquals("2번메세지", result.getContent().get(1).getContent());
  }

  @DisplayName("채팅 메시지 전송 성공 테스트")
  @Test
  void sendMessageSuccess() {
    // given
    Request request = Request.builder()
        .content("내용")
        .build();
    Chat chat = Chat.builder()
        .chatRoom(testChatRoom)
        .user(testUser)
        .type(ChatType.CHAT)
        .content(request.getContent())
        .build();

    when(userRepository.findByIdAndDeletedAtIsNull(eq(testUser.getId()))).thenReturn(Optional.of(testUser));
    when(chatRoomRepository.findById(eq(testChatRoom.getId()))).thenReturn(Optional.of(testChatRoom));
    when(chatRepository.findTopByChatRoomAndUserOrderByCreatedAtDesc(eq(testChatRoom), eq(testUser)))
        .thenReturn(Optional.of(Chat.builder().type(ChatType.ENTER).build()));

    ArgumentCaptor<Chat> chatCaptor = ArgumentCaptor.forClass(Chat.class);
    when(chatRepository.save(chatCaptor.capture())).thenReturn(chat);

    // when
    chatService.sendMessage(testChatRoom.getId(), testUser.getId(), request);

    // then
    verify(chatRepository, times(1)).save(chatCaptor.capture());
    Chat savedChat = chatCaptor.getValue();

    assertEquals(testChatRoom, savedChat.getChatRoom());
    assertEquals(testUser, savedChat.getUser());
    assertEquals(ChatType.CHAT, savedChat.getType());
    assertEquals(request.getContent(), savedChat.getContent());

    verify(messagingTemplate, times(1)).convertAndSend(eq(makeSocketDestination(
        SEND_TO_CLIENT_PREFIX, CHAT_SEPARATOR, testChatRoom.getId())), eq(Information.fromEntity(savedChat)));
  }

  @DisplayName("채팅방 퇴장 성공 테스트")
  @Test
  void leaveChatRoomSuccess() {
    // given
    Chat chat = Chat.builder()
        .chatRoom(testChatRoom)
        .user(testUser)
        .type(ChatType.LEAVE)
        .content(ChatType.LEAVE.getComment(testUser))
        .build();

    when(userRepository.findByIdAndDeletedAtIsNull(eq(testUser.getId()))).thenReturn(Optional.of(testUser));
    when(chatRoomRepository.findById(eq(testChatRoom.getId()))).thenReturn(Optional.of(testChatRoom));
    when(chatRepository.findTopByChatRoomAndUserOrderByCreatedAtDesc(eq(testChatRoom), eq(testUser)))
        .thenReturn(Optional.of(Chat.builder().type(ChatType.ENTER).build()));

    ArgumentCaptor<Chat> chatCaptor = ArgumentCaptor.forClass(Chat.class);
    when(chatRepository.save(chatCaptor.capture())).thenReturn(chat);

    // when
    chatService.leaveChatRoomForChatRoomIdAndUserId(testChatRoom.getId(), testUser.getId());

    // then
    verify(chatRepository, times(1)).save(chatCaptor.capture());
    Chat savedChat = chatCaptor.getValue();

    assertEquals(testChatRoom, savedChat.getChatRoom());
    assertEquals(testUser, savedChat.getUser());
    assertEquals(ChatType.LEAVE, savedChat.getType());
    assertEquals(ChatType.LEAVE.getComment(testUser), savedChat.getContent());

    verify(messagingTemplate, times(1)).convertAndSend(eq(makeSocketDestination(
        SEND_TO_CLIENT_PREFIX, CHAT_SEPARATOR, testChatRoom.getId())), eq(Information.fromEntity(savedChat)));
  }

  @DisplayName("채팅방 메시지 가져오기 실패 테스트(권한 없음)")
  @Test
  void getChatMessagesOnChatRoomFailure() {
    // given
    when(userRepository.findByIdAndDeletedAtIsNull(eq(testUser.getId()))).thenReturn(Optional.of(testUser));
    when(chatRoomRepository.findById(eq(testChatRoom.getId()))).thenReturn(Optional.of(testChatRoom));
    when(chatRepository.findTopByChatRoomAndUserOrderByCreatedAtDesc(eq(testChatRoom), eq(testUser)))
        .thenReturn(Optional.of(Chat.builder().type(ChatType.LEAVE).build()));

    // when & then
    CustomException exception = assertThrows(CustomException.class, () ->
        chatService.getChatMessagesOnChatRoom(testUser.getId(), testChatRoom.getId(), 0, 10)
    );

    assertEquals(ErrorCode.CANNOT_PROCESS_IN_CHATROOM, exception.getErrorCode());
  }

  @DisplayName("채팅방 퇴장 실패 테스트(이미 퇴장한 상태)")
  @Test
  void leaveChatRoomFailure() {
    // given
    when(userRepository.findByIdAndDeletedAtIsNull(eq(testUser.getId()))).thenReturn(Optional.of(testUser));
    when(chatRoomRepository.findById(eq(testChatRoom.getId()))).thenReturn(Optional.of(testChatRoom));
    when(chatRepository.findTopByChatRoomAndUserOrderByCreatedAtDesc(eq(testChatRoom), eq(testUser)))
        .thenReturn(Optional.of(Chat.builder().type(ChatType.LEAVE).build()));

    // when
    chatService.leaveChatRoomForChatRoomIdAndUserId(testChatRoom.getId(), testUser.getId());

    // then
    verify(chatRepository, Mockito.never()).save(any(Chat.class));
    verify(messagingTemplate, Mockito.never()).convertAndSend(anyString(), any(Information.class));
  }

  @DisplayName("채팅방 입장 실패 테스트(이미 입장한 상태)")
  @Test
  void enteredChatRoomAlreadyEntered() {
    // given
    Meeting meeting = Meeting.builder().id(1L).build();

    when(chatRoomRepository.findByMeeting(eq(meeting))).thenReturn(Optional.of(testChatRoom));
    when(chatRepository.findTopByChatRoomAndUserOrderByCreatedAtDesc(eq(testChatRoom), eq(testUser)))
        .thenReturn(Optional.of(Chat.builder().type(ChatType.ENTER).build()));

    // when
    chatService.enteredChatRoom(testUser, meeting);

    // then
    verify(chatRepository, Mockito.never()).save(any(Chat.class));
    verify(messagingTemplate, Mockito.never()).convertAndSend(anyString(), any(Information.class));
  }

  @DisplayName("채팅방 새로 생성 테스트")
  @Test
  void createChatRoomForNewMeeting() {
    // given
    Meeting meeting = Meeting.builder().id(1L).build();
    Chat newChat = Chat.builder()
        .chatRoom(testChatRoom)
        .user(testUser)
        .type(ChatType.ENTER)
        .content(ChatType.ENTER.getComment(testUser))
        .build();

    when(chatRoomRepository.findByMeeting(eq(meeting))).thenReturn(Optional.empty());
    when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(testChatRoom);

    when(chatRepository.findTopByChatRoomAndUserOrderByCreatedAtDesc(eq(testChatRoom), eq(testUser)))
        .thenReturn(Optional.of(Chat.builder().type(ChatType.LEAVE).build()));

    when(chatRepository.save(any(Chat.class))).thenReturn(newChat);

    // when
    chatService.enteredChatRoom(testUser, meeting);

    // then
    ArgumentCaptor<ChatRoom> chatRoomCaptor = ArgumentCaptor.forClass(ChatRoom.class);
    verify(chatRoomRepository, times(1)).save(chatRoomCaptor.capture());
    ChatRoom savedChatRoom = chatRoomCaptor.getValue();

    assertEquals(meeting, savedChatRoom.getMeeting());

    ArgumentCaptor<Chat> chatCaptor = ArgumentCaptor.forClass(Chat.class);
    verify(chatRepository, times(1)).save(chatCaptor.capture());
    Chat savedChat = chatCaptor.getValue();

    assertEquals(testChatRoom, savedChat.getChatRoom());
    assertEquals(testUser, savedChat.getUser());
    assertEquals(ChatType.ENTER, savedChat.getType());
    assertEquals(ChatType.ENTER.getComment(testUser), savedChat.getContent());

    verify(messagingTemplate, times(1)).convertAndSend(
        eq(makeSocketDestination(SEND_TO_CLIENT_PREFIX, CHAT_SEPARATOR, testChatRoom.getId())),
        eq(Information.fromEntity(savedChat))
    );
  }

  @DisplayName("채팅 메시지 전송 실패 테스트(채팅방을 떠난 경우)")
  @Test
  void sendMessageFailedWhenUserLeft() {
    // given
    Request request = Request.builder()
        .content("내용")
        .build();

    when(userRepository.findByIdAndDeletedAtIsNull(eq(testUser.getId()))).thenReturn(Optional.of(testUser));
    when(chatRoomRepository.findById(eq(testChatRoom.getId()))).thenReturn(Optional.of(testChatRoom));
    when(chatRepository.findTopByChatRoomAndUserOrderByCreatedAtDesc(eq(testChatRoom), eq(testUser)))
        .thenReturn(Optional.of(Chat.builder().type(ChatType.LEAVE).build()));

    // when
    CustomException exception = assertThrows(CustomException.class, () ->
        chatService.sendMessage(testChatRoom.getId(), testUser.getId(), request)
    );

    // then
    assertEquals(ErrorCode.CANNOT_PROCESS_IN_CHATROOM, exception.getErrorCode());
    verify(chatRepository, Mockito.never()).save(any(Chat.class));
    verify(messagingTemplate, Mockito.never()).convertAndSend(any(String.class), any(Information.class));
  }
}
