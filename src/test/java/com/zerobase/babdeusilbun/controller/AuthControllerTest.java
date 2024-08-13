package com.zerobase.babdeusilbun.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyPasswordRequest;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyPasswordResponse;
import com.zerobase.babdeusilbun.security.application.AuthApplication;
import com.zerobase.babdeusilbun.security.controller.AuthController;
import com.zerobase.babdeusilbun.security.dto.UserCustomUserDetails;
import com.zerobase.babdeusilbun.security.service.JwtValidationService;
import com.zerobase.babdeusilbun.security.service.impl.SignServiceImpl;
import com.zerobase.babdeusilbun.util.TestUserUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SignServiceImpl signService;
  @MockBean
  private JwtValidationService jwtValidationService;
  @MockBean
  private AuthApplication authApplication;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    //로그인 정보 세팅
    UserCustomUserDetails userDetails = TestUserUtility.createTestUser();

    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
    );
  }

  @DisplayName("비밀번호 확인 테스트")
  @Test
  void passwordConfirm() throws Exception {
    //given
    VerifyPasswordRequest request = new VerifyPasswordRequest("password");
    VerifyPasswordResponse response = VerifyPasswordResponse.builder().isCorrected(true).build();

    //when
    when(signService.passwordConfirm(eq(request), anyString())).thenReturn(response);

    //then
    mockMvc.perform(post("/api/password-confirm")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.isCorrected").value(true));
  }
}
