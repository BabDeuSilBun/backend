package com.zerobase.babdeusilbun.inquiry.dto;

import com.zerobase.babdeusilbun.domain.InquiryImage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class InquiryImageDto {

  private Long imageId;
  private String url;
  private Integer sequence;

  public static InquiryImageDto fromEntity(InquiryImage image) {
    return InquiryImageDto.builder()
        .imageId(image.getId()).url(image.getUrl()).sequence(image.getSequence())
        .build();
  }

}
