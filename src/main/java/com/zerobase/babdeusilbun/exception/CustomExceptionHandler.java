package com.zerobase.babdeusilbun.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException
      (HttpServletRequest request, HttpServletResponse response, CustomException e) {

    log.error("=== ERROR!!! ===");
    log.error("Request URI = {}", request.getRequestURI());
    log.error(e.getMessage());

    response.setStatus(e.getErrorCode().getStatus().value());

    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(ErrorResponse.of(e));
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleBindException
      (HttpServletRequest request, HttpServletResponse response, BindException e) {

    log.error("=== ERROR!!! ===");
    log.error("Request URI = {}", request.getRequestURI());
    log.error(e.getMessage());

    response.setStatus(HttpStatus.BAD_REQUEST.value());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(e));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleBindException
      (HttpServletRequest request, RuntimeException e) {

    log.error("=== ERROR!!! ===");
    log.error("Request URI = {}", request.getRequestURI());
    log.error(e.getMessage());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(e));
  }

}
