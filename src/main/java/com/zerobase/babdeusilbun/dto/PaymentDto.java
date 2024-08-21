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
  public static class Request {

    private PaymentGateway pg;
    private PaymentMethod payMethod;
    private Long point;

  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Builder
  public static class Response {

    private String transactionId;
    private String name;
    private Long price;

    public static Response createNew(String name, Integer price) {
      return Response.builder()
          .transactionId(LocalDate.now() + UUID.randomUUID().toString())
          .name(name).price(price.longValue())
          .build();
    }
  }

}
