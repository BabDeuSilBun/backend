package com.zerobase.backend.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SecurityErrorCode {

  SCHOOL_NOT_FOUND(HttpStatus.BAD_REQUEST, "couldn't find school"),
  MAJOR_NOT_FOUND(HttpStatus.BAD_REQUEST, "couldn't find major");

  private final HttpStatus status;
  private final String message;

  SecurityErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}
