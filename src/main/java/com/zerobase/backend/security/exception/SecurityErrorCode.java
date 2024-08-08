package com.zerobase.backend.security.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SecurityErrorCode {

  //TODO
  // security 관련 없는 error code 분리
  SCHOOL_NOT_FOUND(BAD_REQUEST, "couldn't find school"),
  MAJOR_NOT_FOUND(BAD_REQUEST, "couldn't find major"),

  EMAIL_NOT_FOUND(UNAUTHORIZED, "couldn't find authentication from this email"),
  PASSWORD_NOT_MATCH(UNAUTHORIZED, "this password is wrong"),
  USER_NOT_FOUND(BAD_REQUEST, "couldn't find user"),
  USER_WITHDRAWAL(BAD_REQUEST, "couldn't find user"),
  ENTREPRENEUR_NOT_FOUND(BAD_REQUEST, "couldn't find entrepreneur"),
  ENTREPRENEUR_WITHDRAWAL(BAD_REQUEST, "This entrepreneur has withdrawn"),

  AUTHENTICATION_HEADER_INVALID(BAD_REQUEST, "this is invalid authentication header"),
  JWT_TOKEN_EXPIRED(BAD_REQUEST, "this jwt token is expired"),
  JWT_TOKEN_INVALID(BAD_REQUEST, "this jwt token is invalid"),
  JWT_TOKEN_IS_BLACK(BAD_REQUEST, "this jwt token is logout status"),
  JWT_AND_REFRESH_TOKEN_NOT_MATCH(BAD_REQUEST, "jwt token and refresh token is not match"),
  REFRESH_TOKEN_NOT_FOUND(BAD_REQUEST, "couldn't find refresh token"),
  REFRESH_TOKEN_INVALID(BAD_REQUEST, "this refresh token is invalid"),

  // 회원 탈퇴 관련
  USER_POINT_NOT_EMPTY(CONFLICT, "user's points still remain"),
  USER_MEETING_STILL_LEFT(CONFLICT, "the meeting participated in is still in progress"),
  ENTREPRENEUR_ORDER_PROCEEDING(CONFLICT, "there are still orders in progress")
  ;

  private final HttpStatus status;
  private final String message;

  SecurityErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
