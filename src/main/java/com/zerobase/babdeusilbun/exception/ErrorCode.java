package com.zerobase.babdeusilbun.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
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
  EMAIL_PREFIX_INVALID(UNAUTHORIZED, "this is invalid email prefix"),
  PASSWORD_NOT_MATCH(UNAUTHORIZED, "this password is wrong"),

  USER_WITHDRAWAL(UNAUTHORIZED, "this user has withdrawn"),
  ENTREPRENEUR_WITHDRAWAL(UNAUTHORIZED, "this entrepreneur has withdrawn"),

  AUTHENTICATION_HEADER_INVALID(UNAUTHORIZED, "this is invalid authentication header"),
  JWT_TOKEN_EXPIRED(UNAUTHORIZED, "this jwt token is expired"),
  JWT_TOKEN_INVALID(UNAUTHORIZED, "this jwt token is invalid"),
  JWT_TOKEN_IS_BLACK(UNAUTHORIZED, "this jwt token is logout status"),
  JWT_AND_REFRESH_TOKEN_NOT_MATCH(UNAUTHORIZED, "jwt token and refresh token is not match"),

  REFRESH_TOKEN_COOKIE_NOT_FOUND(UNAUTHORIZED, "couldn't find refresh token cookie"),
  REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED, "couldn't find refresh token"),
  REFRESH_TOKEN_INVALID(UNAUTHORIZED, "this refresh token is invalid"),

  // 회원가입 관련
  EMAIL_DUPLICATED(CONFLICT, "this email is duplicated"),

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
  MEETING_TYPE_INVALID(BAD_REQUEST, "meeting type is invalid"),
  MEETING_LEADER_NOT_MATCH(BAD_REQUEST, "this user is not a leader of that meeting"),
  MEETING_PARTICIPANT_NOT_MATCH(BAD_REQUEST, "this user is not a participant of that meeting"),
  MEETING_PARTICIPANT_EXIST(BAD_REQUEST, "this meeting have participants"),
  MEETING_ALREADY_DELETED(CONFLICT, "meeting is already deleted"),

  // 주문 관련
  PURCHASE_NOT_FOUND(NOT_FOUND, "couldn't find purchase"),
  PURCHASE_STATUS_INVALID(BAD_REQUEST, "purchase status is invalid"),
  PURCHASE_STATUS_CANCEL(NOT_FOUND, "this participant cancel that purchase"),
  PURCHASE_MEETING_NOT_MATCH(BAD_REQUEST, "purchase and meeting are not match"),

  // 개별 주문 관련
  ALREADY_EXIST_INDIVIDUAL_PURCHASE(CONFLICT, "already have individual_purchase which user want to enroll."),

  // 상점 관련,
  STORE_NOT_FOUND(NOT_FOUND, "couldn't find store"),
  ALREADY_EXIST_STORE(CONFLICT, "already have store which user want to enroll."),
  NO_AUTH_ON_STORE(FORBIDDEN, "no auth to use or modify or delete this storage"),
  STORE_IMAGE_NOT_FOUND(NOT_FOUND, "couldn't find store image."),
  NO_IMAGE_ON_STORE(NOT_FOUND, "cannot find image on store which request on."),
  STORE_NOT_INCLUDE_MENU(CONFLICT, "This menu is not included in the store."),

  // 메뉴 관련
  MENU_NOT_FOUND(NOT_FOUND, "couldn't find menu"),
  ALREADY_EXIST_MENU(CONFLICT, "already have menu which user want to enroll."),
  NO_AUTH_ON_MENU(FORBIDDEN, "no auth to use or modify or delete this storage"),

  //이메일 인증 관련
  CANNOT_SEND_MAIL_EXCEEDS_MAX_COUNT(BAD_REQUEST,
      "the number of attempts requested to send mail exceeds the maximum allowed count."),
  FAILED_SEND_MAIL(CONFLICT, "failed to send mail."),

  // 문의 관련
  INQUIRY_NOT_FOUND(NOT_FOUND, "couldn't find inquiry"),
  INQUIRY_WRITER_NOT_MATCH(BAD_REQUEST, "this user is not writer of that inquiry"),

  INQUIRY_IMAGE_NOT_FOUND(NOT_FOUND, "couldn't find inquiry image"),
  INQUIRY_IMAGE_SEQUENCE_INVALID(BAD_REQUEST, "inquiry image sequence is invalid"),
  INQUIRY_IMAGE_AND_INQUIRY_NOT_MATCH(BAD_REQUEST, "image and inquiry is not match"),

  // 평가 관련
  EVALUATE_ALREADY_EXIST(CONFLICT, "already completed the evaluation"),

  // 스냅샷 관련
  PURCHASE_PAYMENT_NOT_FOUND(NOT_FOUND, "couldn't find purchase snapshot"),
  PAYMENT_SNAPSHOT_NOT_FOUND(NOT_FOUND, "couldn't find payment snapshot"),

  // 결제 관련
  IAMPORT_SERVER_ERROR(INTERNAL_SERVER_ERROR, "something wrong occur during connection iamport server"),
  PAYMENT_STATUS_INVALID(BAD_REQUEST, "this status code is invalid"),
  PAYMENT_GATEWAY_INVALID(BAD_REQUEST, "this pg code is invalid"),
  PAYMENT_METHOD_INVALID(BAD_REQUEST, "this pm code is invalid"),
  PAYMENT_INFORMATION_NOT_MATCH(CONFLICT, "payment information is not match"),

  POINT_SHORTAGE(CONFLICT, "not enough points"),

  // Redisson 관련
  REDISSON_LOCK_FAIL_OBTAIN(INTERNAL_SERVER_ERROR, "something wrong occur during get redisson lock"),
  REDISSON_LOCK_TIMEOUT(INTERNAL_SERVER_ERROR, "redisson lock is timeout")
  ;

  private final HttpStatus status;
  private final String message;

  ErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
