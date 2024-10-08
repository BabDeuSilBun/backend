package com.zerobase.babdeusilbun.enums;

import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PaymentStatus {

  READY("ready", "브라우저 창 이탈, 가상계좌 발급 완료 등 미결제 상태"),
  PAID("paid", "결제 완료"),
  FAILED("failed", "신용카드 한도 초과, 체크카드 잔액 부족, 브라우저 창 종료 또는 취소 버튼 클릭 등 결제 실패 상태");

  private final String code;
  private final String description;

  PaymentStatus(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public static PaymentStatus fromCode(String code) {
    return Arrays.stream(PaymentStatus.values())
        .filter(ps -> ps.code.equals(code))
        .findFirst()
        .orElseThrow(() -> new CustomException(PAYMENT_STATUS_INVALID));
  }
}
