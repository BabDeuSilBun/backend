package com.zerobase.babdeusilbun.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyCodeRequest;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyCodeResponse;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyEmailRequest;
import com.zerobase.babdeusilbun.service.EmailService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = EmailController.class)
class EmailControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private EmailService emailService;
  @Autowired
  private ObjectMapper objectMapper;

  @DisplayName("이메일 유효성 검증 이메일 전송")
  @WithMockUser
  @Test
  void sendEmailVerifyCode() throws Exception {
    //given
    VerifyEmailRequest request = new VerifyEmailRequest("test@example.com");

    //when
    doNothing().when(emailService).sendVerificationCode(any(VerifyEmailRequest.class));

    //then
    mockMvc.perform(post("/api/signup/email-verify")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isOk());
  }

  @DisplayName("검증 코드 확인")
  @WithMockUser
  @Test
  void confirmEmailVerifyCode() throws Exception {
    //given
    VerifyCodeRequest request =
        new VerifyCodeRequest("test@example.com", UUID.randomUUID().toString().substring(0, 10));
    VerifyCodeResponse response = VerifyCodeResponse.builder().result(true).build();

    //when
    when(emailService.verifyCode(any(VerifyCodeRequest.class))).thenReturn(response);

    //then
    mockMvc.perform(post("/api/signup/email-verify/confirm")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.result").value(true));
  }
}
