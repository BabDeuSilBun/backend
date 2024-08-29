package com.zerobase.babdeusilbun.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.babdeusilbun.controller.chat.UserChatController;
import com.zerobase.babdeusilbun.dto.ChatDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.ChatService;
import com.zerobase.babdeusilbun.util.TestUserUtility;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(UserChatController.class)
public class UserChatControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ChatService chatService;

  @Autowired
  private ObjectMapper objectMapper;

  private CustomUserDetails testUser;

  @BeforeEach
  void setUp() {
    testUser = TestUserUtility.createTestUser();

    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(testUser, null, testUser.getAuthorities())
    );
  }

  @DisplayName("채팅방 목록 가져오기 테스트")
  @Test
  void getChatRoomsTest() throws Exception {
    Page<ChatDto.RoomInformation> chatRooms = new PageImpl<>(
        Collections.emptyList(), PageRequest.of(0, 10), 0);

    Mockito.when(chatService.getChatRooms(eq(testUser.getId()), eq(0), eq(10)))
        .thenReturn(chatRooms);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/chat-rooms")
            .with(csrf())
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @DisplayName("채팅방 메시지 가져오기 테스트")
  @Test
  void getChatMessagesOnChatRoomTest() throws Exception {
    Page<ChatDto.Information> chatMessages = new PageImpl<>(
        Collections.emptyList(), PageRequest.of(0, 10), 0);

    Mockito.when(chatService.getChatMessagesOnChatRoom(eq(testUser.getId()), eq(1L), eq(0), eq(10)))
        .thenReturn(chatMessages);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/chat-rooms/1")
            .with(csrf())
            .param("page", "0")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @DisplayName("채팅 메시지 전송 테스트")
  @Test
  void sendMessageTest() throws Exception {
    ChatDto.Request request = ChatDto.Request.builder()
        .content("메세지 내용")
        .build();

    doNothing().when(chatService).sendMessage(eq(1L), eq(testUser.getId()), eq(request));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users/chat-rooms/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    Mockito.verify(chatService).sendMessage(eq(1L), eq(testUser.getId()), eq(request));
  }

  @DisplayName("채팅방 퇴장 테스트")
  @Test
  void leaveChatRoomTest() throws Exception {
    doNothing().when(chatService).leaveChatRoomForChatRoomIdAndUserId(eq(1L), eq(testUser.getId()));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users/chat-rooms/1/leave")
            .with(csrf()))
        .andExpect(status().isNoContent());

    Mockito.verify(chatService).leaveChatRoomForChatRoomIdAndUserId(eq(1L), eq(testUser.getId()));
  }
}