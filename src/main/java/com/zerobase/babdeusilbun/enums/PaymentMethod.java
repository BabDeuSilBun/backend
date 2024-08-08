package com.zerobase.babdeusilbun.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {

  CARD("신용카드"),
  TRANS("실시간계좌이체"),
  VBANK("가상계좌"),
  PHONE("휴대폰소액결제"),
  PAYPAL("페이팔 SPB 일반결제"),
  APPLEPAY("애플페이"),
  NAVERPAY("네이버페이"),
  SAMSUNG("삼성페이"),
  KPAY("KPay앱"),
  KAKAOPAY("카카오페이"),
  PAYCO("페이코"),
  LPAY("LPAY"),
  SSGPAY("SSG페이"),
  TOSSPAY("토스간편결제"),
  CULTURELAND("문화상품권"),
  SMARTCULTURE("스마트문상"),
  HAPPYMONY("해피머니"),
  BOOKNLIFE("도서문화상품권"),
  POINT("베네피아 포인트 등 포인트 결제"),
  WECHAT("위쳇페이"),
  ALIPAY("알리페이"),
  UNIONPAY("유니온페이"),
  TENPAY("텐페이");

  private final String description;

  PaymentMethod(String description) {
    this.description = description;
  }
}
