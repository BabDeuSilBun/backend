package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.enums.EmailTemplate.VERIFY_EMAIL;
import static com.zerobase.babdeusilbun.util.EmailUtility.EMAIL_CODE_EXPIRATION_MINUTES;
import static com.zerobase.babdeusilbun.util.EmailUtility.EMAIL_CODE_PREFIX;
import static com.zerobase.babdeusilbun.util.EmailUtility.EMAIL_COUNT_PREFIX;
import static com.zerobase.babdeusilbun.util.EmailUtility.makeKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.dto.SignDto.VerifyCodeRequest;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyCodeResponse;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyEmailRequest;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.service.impl.EmailServiceImpl;
import jakarta.mail.internet.MimeMessage;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
  @Mock
  private JavaMailSender mailSender;
  @Mock
  private StringRedisTemplate redisTemplate;
  @Mock
  private ValueOperations<String, String> valueOperations;
  @Mock
  private SpringTemplateEngine templateEngine;
  @InjectMocks
  private EmailServiceImpl emailService;

  private final String email = "test@example.com";
  private final String code = "1234567890";

  @DisplayName("이메일 유효성 검증 메일 발송 서비스 성공 사례")
  @Test
  void sendVerificationCodeSuccess() {
    //given
    VerifyEmailRequest request = new VerifyEmailRequest(email);
    String codeKey = makeKey(EMAIL_CODE_PREFIX, request.getEmail());
    String countKey = makeKey(EMAIL_COUNT_PREFIX, request.getEmail());
    String html = "<html><body>" + code + "</body></html>";

    //when
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);

    when(valueOperations.get(countKey))
        .thenReturn(null);
    doNothing().when(valueOperations)
        .set(eq(codeKey), anyString(), eq(Duration.ofMinutes(EMAIL_CODE_EXPIRATION_MINUTES)));
    when(mailSender.createMimeMessage())
        .thenReturn(mock(MimeMessage.class));
    when(templateEngine.process(eq(VERIFY_EMAIL.getHtml()), any(Context.class)))
        .thenReturn(html);
    when(valueOperations.increment(eq(countKey)))
        .thenReturn(1L);
    when(redisTemplate.expire(eq(countKey), any()))
        .thenReturn(true);

    emailService.sendVerificationCode(request);

    //then
    verify(valueOperations, times(1))
        .set(eq(codeKey), anyString(), eq(Duration.ofMinutes(EMAIL_CODE_EXPIRATION_MINUTES)));
    verify(valueOperations, times(1))
        .increment(eq(countKey));
    verify(redisTemplate, times(1))
        .expire(eq(countKey), any());
    verify(mailSender, times(1))
        .send(any(MimeMessage.class));
  }

  @DisplayName("이메일 유효성 검증 메일 발송 서비스 실패 사례(최대 횟수 초과)")
  @Test
  void sendVerificationCodeFailed() {
    //given
    VerifyEmailRequest request = new VerifyEmailRequest(email);

    //when
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);

    when(valueOperations.get(makeKey(EMAIL_COUNT_PREFIX, request.getEmail()))).thenReturn(String.valueOf(5));

    //then
    CustomException exception = assertThrows(CustomException.class, () -> {
      emailService.sendVerificationCode(request);
    });
    assertEquals(ErrorCode.CANNOT_SEND_MAIL_EXCEEDS_MAX_COUNT, exception.getErrorCode());
  }

  @DisplayName("이메일 유효성 검증 코드 검증 서비스 일치 사례")
  @Test
  void verifyCodeSuccess() {
    //given
    VerifyCodeRequest request = new VerifyCodeRequest(email, code);

    //when
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);

    when(valueOperations.get(makeKey(EMAIL_CODE_PREFIX, request.getEmail()))).thenReturn(code);
    VerifyCodeResponse response = emailService.verifyCode(request);

    //then
    assertTrue(response.isResult());
  }

  @DisplayName("이메일 유효성 검증 코드 검증 서비스 불일치 사례")
  @Test
  void verifyCodeFailed() {
    //given
    VerifyCodeRequest request = new VerifyCodeRequest(email, code);

    //when
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);

    when(valueOperations.get(makeKey(EMAIL_CODE_PREFIX, request.getEmail()))).thenReturn(null);
    VerifyCodeResponse response = emailService.verifyCode(request);

    //then
    assertFalse(response.isResult());
  }
}
