package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.enums.MeetingStatus;
import com.zerobase.babdeusilbun.enums.PurchaseType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MeetingDto {

  private Long meetingId;
  private Long storeId;

  private List<StoreImageDto> storeImage;

  private String storeName;

  private PurchaseType purchaseType;

  private Integer participantMin;
  private Integer participantMax;

  private Boolean isEarlyPaymentAvailable;
  private LocalDateTime paymentAvailableAt;

  private DeliveryAddressDto deliveryAddress;
  private MetAddressDto metAddress;

  private Integer deliveryFee;
  private LocalDateTime deliveredAt;

  private MeetingStatus status;



}
