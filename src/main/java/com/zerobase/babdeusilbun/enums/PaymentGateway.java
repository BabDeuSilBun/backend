package com.zerobase.babdeusilbun.enums;

import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.exception.CustomException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PaymentGateway {

  // 결제대행사
  DANAL("danal", "다날 휴대폰소액결제 및 휴대폰 본인인증"),
  DANAL_TPAY("danal_tpay", "다날 결제창 일반/정기결제"),
  DAOU("daou", "키움페이 결제창 일반결제 및 API 수기/정기결제"),
  HTML5_INICIS("html5_inicis", "이니시스 결제창 일반/정기결제"),
  INICIS_UNIFIED("inicis_unifled", "이니시스 통합인증"),
  INICIS("inicis", "이니시스 API 수기/정기결제 및 신용카드 본인인증"),
  KCP("kcp", "NHN KCP 결제창 일반/수기결제 및 API 수기/정기결제"),
  KCP_BILLING("kcp_billing", "NHN KCP 결제창 정기결제"),
  KICC("kicc", "이지페이(한국정보통신) 결제창 일반/정기결제"),
  KSNET("ksnet", "KSNET 결제창 일반결제 및 API 수기/정기결제"),
  MOBILIANS("mobilians", "모빌리언스 결제창 일반/정기결제"),
  NICE("nice", "나이스페이먼츠(구모듈) 결제창 일반결제 및 API 수기/정기결제"),
  NICE_V2("nice_v2", "나이스페이(신모듈) 결제창 일반결제 및 API 수기/정기결제"),
  SETTLE("settle", "헥토파이낸셜 결제창 일반결제 및 API 수기/정기결제"),
  SETTLE_ACC("settle_acc", "헥토파이낸셜 내통장결제"),
  SMARTTO("smartto", "스마트로(구모듈) 결제창 일반결제"),
  SMARTRO_V2("smartro_v2", "스마트로(신모듈) 결제창 일반/정기결제 및 API 수기/정기결제"),
  TOSSPAYMENTS("tosspayments", "토스페이먼츠(신모듈) 결제장 일반/수기/정기결제 및 API 일반/수기/정기결제"),
  TOSS_BRANDPAY("toss_brandpay", "토스페이먼츠 브랜드페이"),
  UPLUS("uplus", "토스페이먼츠(구모듈) 결제창 일반결제"),
  WELCOME("welcome", "웰컴페이먼츠 결제창 일반/정기결제 및 API 일반/정기결제"),

  // 간편결제 직연동
  TOSSPAY("tosspay", "토스페이 일반결제"),
  TOSSPAY_V2("tosspay_v2", "토스페이 일반/정기결제"),
  PAYCO("payco", "페이코 일반/정기결제"),
  KAKAOPAY("kakaopay", "카카오페이 일반/정기결제"),
  NAVERPAY("naverpay", "네이버페이-결제형"),
  NAVERCO("naverco", "네이버페이-주문형"),
  SMILEPAY("smilepay", "스마일페이 일반/정기결제"),

  // 해외 결제대행사
  PAYPAL("paypal", "페이팔(ExpressCheckout) 결제창 일반결제"),
  PAYPAL_V2("paypal_v2", "페이팔(SPB/RT) 결제창 일반/정기결제"),
  EXIMBAY("eximbay", "엑심베이 결제창 일반결제"),
  PAYMENTWA11("paymentwa11", "페이먼트월 결제창 일반 및 API 수기/정기결제");

  private final String code;
  private final String description;

  PaymentGateway(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public static PaymentGateway fromCode(String code) {
    return Arrays.stream(PaymentGateway.values())
        .filter(pg -> pg.code.equals(code))
        .findFirst()
        .orElseThrow(() -> new CustomException(PAYMENT_GATEWAY_INVALID));
  }
}
