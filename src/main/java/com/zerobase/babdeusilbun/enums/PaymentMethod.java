package com.zerobase.babdeusilbun.enums;

import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PaymentMethod {

  CARD("card", "신용카드"),
  TRANS("trans", "실시간계좌이체"),
  VBANK("vbank", "가상계좌"),
  PHONE("phone", "휴대폰소액결제"),
  PAYPAL("paypal", "페이팔 SPB 일반결제"),
  APPLEPAY("applepay", "애플페이"),
  NAVERPAY("naverpay", "네이버페이"),
  SAMSUNG("samsungpay", "삼성페이"),
  KPAY("kpay", "KPay앱"),
  KAKAOPAY("kakaopay", "카카오페이"),
  PAYCO("payco", "페이코"),
  LPAY("lpay", "LPAY"),
  SSGPAY("ssgpay", "SSG페이"),
  TOSSPAY("tosspay", "토스간편결제"),
  CULTUREGIFT("culturegift", "문화상품권"),
  SMARTCULTURE("smartculture", "스마트문상"),
  BOOKNLIFE("booklife", "도서문화상품권"),
  POINT("point", "베네피아 포인트 등 포인트 결제"),
  WECHAT("wechat", "위쳇페이"),
  ALIPAY("alipay", "알리페이"),
  UNIONPAY("unionpay", "유니온페이");

  private final String code;
  private final String description;

  PaymentMethod(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public static PaymentMethod fromCode(String code) {
    return Arrays.stream(PaymentMethod.values())
        .filter(pm -> pm.code.equals(code))
        .findFirst()
        .orElseThrow(() -> new CustomException(PAYMENT_METHOD_INVALID));
  }
}
