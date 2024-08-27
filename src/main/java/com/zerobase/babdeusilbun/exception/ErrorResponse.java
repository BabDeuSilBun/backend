package com.zerobase.babdeusilbun.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private final String status;

    private final String message;

    private final String errorCode;


    public static ErrorResponse of(CustomException e) {
        return ErrorResponse.builder()
                .status("error")
                .message(e.getErrorCode().getMessage())
                .errorCode(e.getErrorCode().name())
                .build();
    }

    public static ErrorResponse of(Exception e) {
        return ErrorResponse.builder()
                .status("error")
                .message(e.getMessage())
                .errorCode(e.getClass().getSimpleName())
                .build();
    }
}
