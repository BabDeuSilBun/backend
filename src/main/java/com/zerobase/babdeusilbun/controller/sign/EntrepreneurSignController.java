package com.zerobase.babdeusilbun.controller.sign;

import static org.springframework.http.HttpStatus.CREATED;

import com.zerobase.babdeusilbun.security.dto.EmailCheckDto;
import com.zerobase.babdeusilbun.security.dto.EmailCheckDto.Response;
import com.zerobase.babdeusilbun.security.dto.SignRequest;
import com.zerobase.babdeusilbun.security.service.SignService;
import com.zerobase.babdeusilbun.swagger.annotation.sign.EntrepreneurSignSwagger.BusinessSignupSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.sign.EntrepreneurSignSwagger.CheckEmailForBusinessSwagger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/businesses")
@RequiredArgsConstructor
public class EntrepreneurSignController {
  private final SignService signService;

  /**
   * 사업자 이메일 중복확인
   */
  @PostMapping("/email-duplicated")
  @CheckEmailForBusinessSwagger
  public ResponseEntity<Response> checkEmailForBusiness(@Validated @RequestBody EmailCheckDto.Request request) {

    return ResponseEntity.ok(
        EmailCheckDto.Response.of(signService.isEntrepreneurEmailIsUnique(request.getEmail()))
    );
  }

  /**
   * 사업자 회원가입
   */
  @PostMapping("/signup")
  @BusinessSignupSwagger
  public ResponseEntity<Void> businessSignup(
      @Validated @RequestBody SignRequest.BusinessSignUp request) {

    signService.entrepreneurSignUp(request);

    return ResponseEntity.status(CREATED).build();
  }
}
