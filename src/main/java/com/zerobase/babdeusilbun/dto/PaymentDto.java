package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.enums.PaymentGateway;
import com.zerobase.babdeusilbun.enums.PaymentMethod;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentDto {

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class Temporary {

    private String transactionId;
    private String name;
    private Long price;
    private Long point;
    private PaymentGateway pg;
    private PaymentMethod payMethod;

    public static Temporary fromDto(ProcessRequest processRequest, ProcessResponse processResponse) {
      return Temporary.builder()
          .transactionId(processResponse.getTransactionId())
          .name(processResponse.getName())
          .price(processResponse.getPrice())
          .point(processRequest.getPoint())
          .pg(processRequest.getPg())
          .payMethod(processRequest.getPayMethod())
          .build();
    }
  }


  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class ProcessRequest {

    private PaymentGateway pg;
    private PaymentMethod payMethod;
    private Long point;

  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class ProcessResponse {

    private String transactionId;
    private String name;
    private Long price;

    public static ProcessResponse createNew(String name, Integer price) {
      return ProcessResponse.builder()
          .transactionId(LocalDate.now() + UUID.randomUUID().toString())
          .name(name).price(price.longValue())
          .build();
    }
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class ConfirmRequest {

    private String transactionId;
    private String portoneUid;

  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class ConfirmResponse {

    private String transactionId;
    private Boolean success;

    public static ConfirmResponse createWhenSuccess(String transactionId) {
      return ConfirmResponse.builder().transactionId(transactionId).success(true).build();
    }

    public static ConfirmResponse createWhenFail(String transactionId) {
      return ConfirmResponse.builder().transactionId(transactionId).success(false).build();
    }
  }

}
