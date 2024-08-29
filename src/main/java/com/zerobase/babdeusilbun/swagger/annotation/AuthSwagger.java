package com.zerobase.babdeusilbun.swagger.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface AuthSwagger {

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "비밀번호 확인")
  @Tag(name = "Auth")
  @interface PasswordConfirmSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "사용자 이메일 중복확인")
  @Tag(name = "Auth")
  @interface CheckEmailForUserSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "사업자 이메일 중복확인")
  @Tag(name = "Auth")
  @interface CheckEmailForBusinessSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "사용자 회원가입")
  @Tag(name = "Auth")
  @interface UserSignupSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "사업자 회원가입")
  @Tag(name = "Auth")
  @interface BusinessSignupSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "사용자 로그인")
  @Tag(name = "Auth")
  @interface UserSigninSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "사업자 로그인")
  @Tag(name = "Auth")
  @interface BusinessSigninSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "로그아웃")
  @Tag(name = "Auth")
  @interface LogoutSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "사용자 회원탈퇴")
  @Tag(name = "Auth")
  @interface UserWithdrawalSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "사업자 회원탈퇴")
  @Tag(name = "Auth")
  @interface EntrepreneurWithdrawalSwagger {

  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  @Operation(summary = "토큰 재발급")
  @Tag(name = "Auth")
  @interface RefreshTokenSwagger {

  }


}
