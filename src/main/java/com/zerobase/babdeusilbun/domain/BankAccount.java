package com.zerobase.babdeusilbun.domain;

import com.zerobase.babdeusilbun.enums.Bank;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
public class BankAccount {
  @Enumerated(EnumType.STRING)
  private Bank bank;

  private String accountOwner;

  private String accountNumber;

}
