package com.zerobase.babdeusilbun.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  SCHOOL_NOT_FOUND(NOT_FOUND, "couldn't find school"),

  MAJOR_NOT_FOUND(NOT_FOUND, "couldn't find major"),

  USER_NOT_FOUND(NOT_FOUND, "couldn't find user"),

  ENTREPRENEUR_NOT_FOUND(NOT_FOUND, "couldn't find entrepreneur"),

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

  // 모임 관련
  MEETING_NOT_FOUND(NOT_FOUND, "couldn't find meeting"),
  MEETING_STATUS_INVALID(BAD_REQUEST, "meeting status is invalid"),
  MEETING_LEADER_NOT_MATCH(BAD_REQUEST, "this user is not a leader of that meeting"),
  MEETING_PARTICIPANT_EXIST(BAD_REQUEST, "this meeting have participants"),

  // 주문 관련
  PURCHASE_NOT_FOUND(NOT_FOUND, "couldn't find purchase"),
  PURCHASE_PAYMENT_NOT_FOUND(NOT_FOUND, "couldn't find purchase snapshot"),

  // 상점 관련
  STORE_NOT_FOUND(NOT_FOUND, "couldn't find store"),
  ALREADY_EXIST_STORE(CONFLICT, "already have store which user want to enroll."),

  //이메일 인증 관련
  CANNOT_SEND_MAIL_EXCEEDS_MAX_COUNT(BAD_REQUEST,
      "the number of attempts requested to send mail exceeds the maximum allowed count."),
  FAILED_SEND_MAIL(CONFLICT, "failed to send mail."),

  // 문의 관련
  INQUIRY_NOT_FOUND(NOT_FOUND, "couldn't find inquiry"),
  INQUIRY_WRITER_NOT_MATCH(BAD_REQUEST, "this user is not writer of that inquiry"),

  INQUIRY_IMAGE_NOT_FOUND(NOT_FOUND, "couldn't find inquiry image"),
  INQUIRY_IMAGE_SEQUENCE_INVALID(BAD_REQUEST, "inquiry image sequence is invalid"),
  INQUIRY_IMAGE_AND_INQUIRY_NOT_MATCH(BAD_REQUEST, "image and inquiry is not match")
  ;

  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
