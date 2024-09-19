package com.zerobase.babdeusilbun.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreImage;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class StoreDto {
  @Data
  @Builder
  public static class Information {
    private Long storeId;
    private Long entrepreneurId;
    private String name;
    private List<StoreImageDto> image;
    private String description;
    private long minPurchasePrice;
    private int minDeliveryTime;
    private int maxDeliveryTime;
    private String deliveryTimeRange;
    private long deliveryPrice;
    private AddressDto address;
    private String phoneNumber;
    private LocalTime openTime;
    private LocalTime closeTime;

    public static Information fromEntity(Store store, List<StoreImage> imageList) {
      return Information.builder()
          .storeId(store.getId())
          .entrepreneurId(store.getEntrepreneur().getId())
          .name(store.getName())
          .image(imageList.stream().map(StoreImageDto::fromEntity).toList())
          .description(store.getDescription())
          .minPurchasePrice(store.getMinPurchaseAmount())
          .minDeliveryTime(store.getMinDeliveryTime())
          .maxDeliveryTime(store.getMaxDeliveryTime())
          .deliveryTimeRange(store.getMinDeliveryTime() + "분 ~ " + store.getMaxDeliveryTime() + "분")
          .deliveryPrice(store.getDeliveryPrice())
          .address(AddressDto.fromEntity(store.getAddress()))
          .phoneNumber(store.getPhoneNumber())
          .openTime(store.getOpenTime())
          .closeTime(store.getCloseTime())
          .build();
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class CreateRequest {
    private String name;
    private String description;
    private long minPurchasePrice;
    private int minDeliveryTime;
    private int maxDeliveryTime;
    private long deliveryPrice;
    private AddressDto address;
    private String phoneNumber;
    @Schema(type = "string", pattern = "HH:mm")
    private LocalTime openTime;
    @Schema(type = "string", pattern = "HH:mm")
    private LocalTime closeTime;

    public Store toEntity(Entrepreneur entrepreneur) {
      return Store.builder()
          .name(name)
          .entrepreneur(entrepreneur)
          .description(description)
          .minPurchaseAmount(minPurchasePrice)
          .minDeliveryTime(minDeliveryTime)
          .maxDeliveryTime(maxDeliveryTime)
          .deliveryPrice(deliveryPrice)
          .address(address.toEntity())
          .phoneNumber(phoneNumber)
          .openTime(openTime)
          .closeTime(closeTime)
          .build();
    }
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class UpdateRequest {
    private String name;
    private String description;
    private Long minPurchasePrice;
    private Integer minDeliveryTime;
    private Integer maxDeliveryTime;
    private Long deliveryPrice;
    private AddressDto address;
    private String phoneNumber;
    @Schema(type = "string", pattern = "HH:mm")
    private LocalTime openTime;
    @Schema(type = "string", pattern = "HH:mm")
    private LocalTime closeTime;
    private Set<Long> categoryIds;
    private Set<Long> schoolIds;
  }

  @Data
  @Builder
  public static class ImageUrl {
    private String url;
    private Integer sequence;
    @JsonProperty("isRepresentative")
    private boolean isRepresentative;

    public StoreImage toEntity(Store store) {
      return StoreImage.builder()
          .store(store)
          .url(url)
          .sequence(sequence)
          .isRepresentative(isRepresentative)
          .build();
    }
  }

  @Data
  @Builder
  public static class IdResponse {
    private Long storeId;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SimpleInformation {
    private Long storeId;
    private String name;
    private String image;
    private int unprocessedPurchaseCount;

    @QueryProjection
    public SimpleInformation(Long storeId, String name, String image, Long unprocessedPurchaseCount) {
      this.storeId = storeId;
      this.name = name;
      this.image = image;
      this.unprocessedPurchaseCount = (unprocessedPurchaseCount == null) ? 0 : unprocessedPurchaseCount.intValue();
    }
  }

  @Data
  @Builder
  public static class PrincipalInformation {
    private Long storeId;
    private Long entrepreneurId;
    private String name;
    private String description;
    private Long minPurchasePrice;
    private int minDeliveryTime;
    private int maxDeliveryTime;
    private String deliveryTimeRange;
    private Long deliveryPrice;
    private AddressDto address;
    private String phoneNumber;
    private String openTime;
    private String closeTime;

    public static PrincipalInformation fromEntity(Store store) {
      return PrincipalInformation.builder()
          .storeId(store.getId())
          .entrepreneurId((store.getEntrepreneur() != null) ? store.getEntrepreneur().getId() : null)
          .name(store.getName())
          .description(store.getDescription())
          .minPurchasePrice(store.getMinPurchaseAmount())
          .minDeliveryTime(store.getMinDeliveryTime())
          .maxDeliveryTime(store.getMaxDeliveryTime())
          .deliveryTimeRange(store.getMinDeliveryTime() + "분 ~ " + store.getMaxDeliveryTime() + "분")
          .deliveryPrice(store.getDeliveryPrice())
          .address(AddressDto.fromEntity(store.getAddress()))
          .phoneNumber(store.getPhoneNumber())
          .openTime((store.getOpenTime() != null) ? store.getOpenTime().toString() : null)
          .closeTime((store.getCloseTime() != null) ? store.getCloseTime().toString() : null)
          .build();
    }
  }
}
