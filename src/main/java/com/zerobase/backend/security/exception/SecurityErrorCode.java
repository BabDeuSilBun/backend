package com.zerobase.backend.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SecurityErrorCode {

  SCHOOL_NOT_FOUND(HttpStatus.BAD_REQUEST, "couldn't find school"),
  MAJOR_NOT_FOUND(HttpStatus.BAD_REQUEST, "couldn't find major"),

  EMAIL_NOT_FOUND(HttpStatus.UNAUTHORIZED, "couldn't find authentication from this email"),
  PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "this password is wrong"),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "couldn't find user"),
  ENTREPRENEUR_NOT_FOUND(HttpStatus.BAD_REQUEST, "couldn't find entrepreneur"),

  AUTHENTICATION_HEADER_INVALID(HttpStatus.BAD_REQUEST, "this is invalid authentication header"),
  JWT_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "this jwt token is expired"),
  JWT_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "this jwt token is invalid"),
  REFRESH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "couldn't find refresh token"),
  REFRESH_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "this refresh token is invalid"),
  JWT_AND_REFRESH_TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "jwt token and refresh token is not match"),

  ;

  private final HttpStatus status;
  private final String message;

  SecurityErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
