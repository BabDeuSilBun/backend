package com.zerobase.babdeusilbun.controller.sign;

import static org.springframework.http.HttpStatus.CREATED;

import com.zerobase.babdeusilbun.security.dto.EmailCheckDto;
import com.zerobase.babdeusilbun.security.dto.EmailCheckDto.Response;
import com.zerobase.babdeusilbun.security.dto.SignRequest;
import com.zerobase.babdeusilbun.security.service.SignService;
import com.zerobase.babdeusilbun.swagger.annotation.sign.UserSignSwagger.CheckEmailForUserSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.sign.UserSignSwagger.UserSignupSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserSignController {
  private final SignService signService;

  /**
   * 사용자 이메일 중복확인
   */
  @PostMapping("/email-duplicated")
  @CheckEmailForUserSwagger
  public ResponseEntity<Response> checkEmailForUser(@Validated @RequestBody EmailCheckDto.Request request) {

    return ResponseEntity.ok(
        EmailCheckDto.Response.of(signService.isUserEmailIsUnique(request.getEmail()))
    );
  }

  /**
   * 사용자 회원가입
   */
  @PostMapping("/signup")
  @UserSignupSwagger
  public ResponseEntity<Void> userSignup(@Validated @RequestBody SignRequest.UserSignUp request) {

    signService.userSignUp(request);

    return ResponseEntity.status(CREATED).build();
  }
}
