package com.zerobase.babdeusilbun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerobase.babdeusilbun.domain.StoreImage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreImageDto {

  private Long imageId;
  private String url;

  public static StoreImageDto fromEntity(StoreImage storeImage) {
    return StoreImageDto.builder()
        .imageId(storeImage.getId())
        .url(storeImage.getUrl())
        .build();
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdateRequest {
    private Integer sequence;
    @JsonProperty("isRepresentative")
    private Boolean isRepresentative;
  }

  public interface Information {
    @Value("#{target.id}")
    Long getImageId();
    String getUrl();
    int getSequence();
    boolean getIsRepresentative();
  }

  public interface Thumbnail {
    @Value("#{target.id}")
    Long getImageId();
    String getUrl();
  }
}
