package com.zerobase.babdeusilbun.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
  @ExceptionHandler(CustomException.class)
  protected ResponseEntity<String> handleCustomException(CustomException e) {
    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(e.getMessage());
  }
}
