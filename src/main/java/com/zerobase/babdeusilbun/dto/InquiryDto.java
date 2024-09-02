package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.Inquiry;
import com.zerobase.babdeusilbun.enums.InquiryStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class InquiryDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class Request {

    @NotBlank(message = "title에는 빈 값이 올 수 없습니다.")
    private String title;
    @NotBlank(message = "content에는 빈 값이 올 수 없습니다.")
    private String content;

    //TODO
    // 배포 때 삭제
    @Override
    public String toString() {
      return "Request{" +
          "title='" + title + '\'' +
          ", content='" + content + '\'' +
          '}';
    }
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class Response {

    private Long inquiryId;

    private String title;
    private String content;
    private String answer;

    private InquiryStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InquiryDto.Response fromEntity(Inquiry inquiry) {
      return Response.builder()
          .inquiryId(inquiry.getId())
          .title(inquiry.getTitle())
          .content(inquiry.getContent())
          .answer(inquiry.getAnswer())
          .status(inquiry.getStatus())
          .createdAt(inquiry.getCreatedAt())
          .updatedAt(inquiry.getUpdatedAt())
          .build();
    }
  }
}
