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
    log.error("[{}][{}]][{}]", request.getRequestURI(), getClientIp(request), e.getMessage());

    response.setStatus(e.getErrorCode().getStatus().value());

    return ResponseEntity
        .status(e.getErrorCode().getStatus())
        .body(ErrorResponse.of(e));
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleBindException
      (HttpServletRequest request, HttpServletResponse response, BindException e) {

    log.error("=== ERROR!!! ===");
    log.error("[{}][{}]][{}]", request.getRequestURI(), getClientIp(request), e.getMessage());

    response.setStatus(HttpStatus.BAD_REQUEST.value());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(e));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleBindException
      (HttpServletRequest request, RuntimeException e) {

    log.error("=== ERROR!!! ===");
    log.error("[{}][{}]][{}]", request.getRequestURI(), getClientIp(request), e.getMessage());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of(e));
  }

  private String getClientIp(HttpServletRequest request) {
    String ipAddress = request.getHeader("X-Forwarded-For");
    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("Proxy-Client-IP");
    }
    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getRemoteAddr();
    }

    // IP가 여러 개일 경우, 첫 번째 IP만 반환
    if (ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
      ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
    }

    return ipAddress;
  }

}
