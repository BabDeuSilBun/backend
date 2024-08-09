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

  ENTREPRENEUR_NOT_FOUND(BAD_REQUEST, "couldn't find entrepreneur"),

  PARAMETER_INVALID(BAD_REQUEST, "this is wrong parameter"),

  // 시큐리티 관련,
  EMAIL_NOT_FOUND(UNAUTHORIZED, "couldn't find authentication from this email"),
  PASSWORD_NOT_MATCH(UNAUTHORIZED, "this password is wrong"),

  USER_WITHDRAWAL(UNAUTHORIZED, "this user has withdrawn"),
  ENTREPRENEUR_WITHDRAWAL(UNAUTHORIZED, "this entrepreneur has withdrawn"),

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
  ENTREPRENEUR_ORDER_PROCEEDING(CONFLICT, "there are still orders in progress"),

  //S3 스토리지 업로드 관련
  FAILED_DELETE_FILE(CONFLICT, "failed to delete file from storage."),
  FAILED_UPLOAD_FILE(CONFLICT, "failed to upload file to storage."),
  INVALID_FILE_EXTENSION(BAD_REQUEST, "invalid file extension to upload"),
  CANNOT_UPLOAD_IMAGE_EXCEEDS_MAX_COUNT(BAD_REQUEST,
      "the number of images requested to upload exceeds the maximum allowed number."),

  // 모임 관련,
  MEETING_NOT_FOUND(BAD_REQUEST, "couldn't find meeting")
  ;

  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
