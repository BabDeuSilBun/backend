package com.zerobase.babdeusilbun.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateAddress;
import com.zerobase.babdeusilbun.dto.UserDto.UpdateRequest;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.UserService;
import com.zerobase.babdeusilbun.util.TestUserUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  private CustomUserDetails testUser;

  @BeforeEach
  void setUp() {
    //로그인 정보 세팅
    testUser = TestUserUtility.createTestUser();

    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(testUser, null, testUser.getAuthorities())
    );
  }

  @DisplayName("내 정보 수정 컨트롤러 테스트(완전 성공)")
  @Test
  void updateProfileSuccess() throws Exception {
    //given
    UpdateRequest input = new UpdateRequest(
        "nickname", "password", "url", "01012345678", 1L, 1L);
    MockMultipartFile file = new MockMultipartFile(
        "file", "image.png", "image/png", "image content".getBytes());
    MockMultipartFile request = new MockMultipartFile(
        "request", "request", "application/json",
        objectMapper.writeValueAsString(input).getBytes());

    //when
    when(userService.updateProfile(eq(testUser.getId()), eq(file), eq(input))).thenReturn(input);

    //then
    mockMvc.perform(multipart("/api/users")
            .file(file)
            .file(request)
            .with(csrf())
            .with(httpRequest -> {
              httpRequest.setMethod("PATCH");
              return httpRequest;
            }))
        .andExpect(status().isOk());
  }

  @DisplayName("내 정보 수정 컨트롤러 테스트(부분 성공)")
  @Test
  void updateProfilePartialSuccess() throws Exception {
    //given
    UpdateRequest input = new UpdateRequest(
        "nickname", "password", "url", "01012345678", 1L, 1L);
    MockMultipartFile file = new MockMultipartFile(
        "file", "image.png", "image/png", "image content".getBytes());
    MockMultipartFile request = new MockMultipartFile(
        "request", "request", "application/json",
        objectMapper.writeValueAsString(input).getBytes());
    UpdateRequest changeRequest = new UpdateRequest(
        "nickname", "password", null, "01012345678", null, null);

    //when
    when(userService.updateProfile(eq(testUser.getId()), eq(file), eq(input))).thenReturn(input);

    //then
    mockMvc.perform(multipart("/api/users")
            .file(file)
            .file(request)
            .with(csrf())
            .with(httpRequest -> {
              httpRequest.setMethod("PATCH");
              return httpRequest;
            }))
        .andExpect(status().isPartialContent());
  }

  @DisplayName("내 주소 수정 컨트롤러 테스트")
  @Test
  void updateAddressSuccess() throws Exception {
    // given
    UpdateAddress input = new UpdateAddress("postal", "streetAddress", "detailAddress");
    MockMultipartFile request = new MockMultipartFile(
            "request", "request", "application/json",
            objectMapper.writeValueAsString(input).getBytes());

    // when
    when(userService.updateAddress(eq(testUser.getId()), eq(input))).thenReturn(input);

    // then
    mockMvc.perform(
            put("/api/users/address")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk());
  }
}
