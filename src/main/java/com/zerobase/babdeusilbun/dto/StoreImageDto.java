package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.StoreImage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
