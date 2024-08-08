package com.zerobase.babdeusilbun.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  SCHOOL_NOT_FOUND(BAD_REQUEST, "couldn't find school"),

  MAJOR_NOT_FOUND(BAD_REQUEST, "couldn't find major"),

  USER_NOT_FOUND(BAD_REQUEST, "couldn't find user"),
  USER_WITHDRAWAL(UNAUTHORIZED, "this user has withdrawn"),

  ENTREPRENEUR_NOT_FOUND(BAD_REQUEST, "couldn't find entrepreneur"),
  ENTREPRENEUR_WITHDRAWAL(UNAUTHORIZED, "this entrepreneur has withdrawn"),

  EMAIL_NOT_FOUND(UNAUTHORIZED, "couldn't find authentication from this email"),
  PASSWORD_NOT_MATCH(UNAUTHORIZED, "this password is wrong"),

  AUTHENTICATION_HEADER_INVALID(UNAUTHORIZED, "this is invalid authentication header"),
  JWT_TOKEN_EXPIRED(UNAUTHORIZED, "this jwt token is expired"),
  JWT_TOKEN_INVALID(UNAUTHORIZED, "this jwt token is invalid"),
  JWT_TOKEN_IS_BLACK(UNAUTHORIZED, "this jwt token is logout status"),
  JWT_AND_REFRESH_TOKEN_NOT_MATCH(UNAUTHORIZED, "jwt token and refresh token is not match"),
  REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED, "couldn't find refresh token"),
  REFRESH_TOKEN_INVALID(UNAUTHORIZED, "this refresh token is invalid"),

  // 회원 탈퇴 관련
  USER_POINT_NOT_EMPTY(CONFLICT, "user's points still remain"),
  USER_MEETING_STILL_LEFT(CONFLICT, "the meeting participated in is still in progress"),
  ENTREPRENEUR_ORDER_PROCEEDING(CONFLICT, "there are still orders in progress")
  ;

  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
