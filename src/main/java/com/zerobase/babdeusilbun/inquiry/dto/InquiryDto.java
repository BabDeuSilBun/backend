package com.zerobase.babdeusilbun.inquiry.dto;

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

  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class ListResponse {

    private Long inquiryId;

    private String title;

    private InquiryStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InquiryDto.ListResponse fromEntity(Inquiry inquiry) {
      return ListResponse.builder()
          .inquiryId(inquiry.getId())
          .title(inquiry.getTitle())
          .status(inquiry.getStatus())
          .createdAt(inquiry.getCreatedAt())
          .updatedAt(inquiry.getUpdatedAt())
          .build();
    }

  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class DetailResponse {

    private Long inquiryId;

    private String title;
    private String content;
    private String answer;

    private InquiryStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InquiryDto.DetailResponse fromEntity(Inquiry inquiry) {
      return DetailResponse.builder()
          .inquiryId(inquiry.getId())
          .title(inquiry.getTitle())
          .content(inquiry.getContent())
          .status(inquiry.getStatus())
          .createdAt(inquiry.getCreatedAt())
          .updatedAt(inquiry.getUpdatedAt())
          .build();
    }

  }

}
