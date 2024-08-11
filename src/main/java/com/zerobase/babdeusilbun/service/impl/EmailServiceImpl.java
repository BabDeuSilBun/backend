package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.util.EmailUtility.EMAIL_CODE_EXPIRATION_MINUTES;
import static com.zerobase.babdeusilbun.util.EmailUtility.EMAIL_CODE_PREFIX;
import static com.zerobase.babdeusilbun.util.EmailUtility.EMAIL_COUNT_PREFIX;
import static com.zerobase.babdeusilbun.util.EmailUtility.EMAIL_VERIFY_MAX_COUNT;
import static com.zerobase.babdeusilbun.util.EmailUtility.untilNextDay;

import com.zerobase.babdeusilbun.dto.SignDto;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyCodeRequest;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyEmailRequest;
import com.zerobase.babdeusilbun.enums.EmailTemplate;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
  private final JavaMailSender mailSender;
  private final StringRedisTemplate redisTemplate;
  private final SpringTemplateEngine templateEngine;

  @Override
  public void sendVerificationCode(VerifyEmailRequest request) {
    String codeKey = EMAIL_CODE_PREFIX + request.getEmail();
    String countKey = EMAIL_COUNT_PREFIX + request.getEmail();

    //발송 횟수 체크
    String countStr = redisTemplate.opsForValue().get(countKey);
    int count = (countStr != null) ? Integer.parseInt(countStr) : 0;

    if (count >= EMAIL_VERIFY_MAX_COUNT) {
      throw new CustomException(ErrorCode.CANNOT_SEND_MAIL_EXCEEDS_MAX_COUNT);
    }

    String code = generateRandomCode();
    redisTemplate.opsForValue().set(codeKey, code, Duration.ofMinutes(EMAIL_CODE_EXPIRATION_MINUTES));

    //코드 발송
    sendCodeMail(request.getEmail(), EmailTemplate.VERIFY_EMAIL, code);
    
    //인증 메일 발송 횟수 증가
    redisTemplate.opsForValue().increment(countKey);
    redisTemplate.expire(countKey, untilNextDay());
  }

  @Override
  public SignDto.VerifyCodeResponse verifyCode(VerifyCodeRequest request) {
    String codeKey = EMAIL_CODE_PREFIX + request.getEmail();
    String savedCode = redisTemplate.opsForValue().get(codeKey);

    return SignDto.VerifyCodeResponse.builder()
        .result(savedCode != null && savedCode.equals(request.getCode()))
        .build();
  }

  //랜덤 코드 생성
  private String generateRandomCode() {
    return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
  }

  private void sendCodeMail(String to, EmailTemplate template, String code) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();

    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
      mimeMessageHelper.setTo(to);
      mimeMessageHelper.setSubject(template.getSubject()); // 메일 제목
      mimeMessageHelper.setText(setContext(code, template.getHtml()), true); // 메일 본문 내용, HTML 여부

      mailSender.send(mimeMessage);
    } catch (MessagingException e) {
      throw new CustomException(ErrorCode.FAILED_SEND_MAIL);
    }
  }

  // thymeleaf 를 통한 html 적용
  private String setContext(String code, String type) {
    Context context = new Context();
    context.setVariable("code", code);

    return templateEngine.process(type, context);
  }
}
