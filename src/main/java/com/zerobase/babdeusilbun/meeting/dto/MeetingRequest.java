package com.zerobase.babdeusilbun.meeting.dto;

import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.DeliveryAddressDto;
import com.zerobase.babdeusilbun.dto.MetAddressDto;
import com.zerobase.babdeusilbun.enums.PurchaseType;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MeetingRequest {

  @NotNull(message = "storeId는 Null 값이 올 수 없습니다.")
  private Long storeId;

  @NotNull(message = "purchaseType은 Null 값이 올 수 없습니다.")
  private PurchaseType purchaseType;

  @NotNull(message = "minHeadcount는 Null 값이 올 수 없습니다.")
  @Min(value = 1, message = "minHeadcount 값은 1 이상이어야 합니다.")
  private Integer minHeadcount;
  @Min(value = 1, message = "maxHeadcount 값은 1 이상이어야 합니다.")
  private Integer maxHeadcount;

  @NotNull(message = "isEarlyPaymentAvailable은 Null 값이 올 수 없습니다.")
  private Boolean isEarlyPaymentAvailable;

  @NotNull(message = "paymentAvailableAt은 Null 값이 올 수 없습니다.")
  private LocalDateTime paymentAvailableAt;

  @NotNull(message = "deliveryAddress는 Null 값이 올 수 없습니다.")
  private DeliveryAddressDto deliveryAddress;
  @NotNull(message = "metAddress는 Null 값이 올 수 없습니다.")
  private MetAddressDto metAddress;


}
