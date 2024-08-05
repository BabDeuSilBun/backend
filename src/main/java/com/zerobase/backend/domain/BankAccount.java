package com.zerobase.backend.domain;

import com.zerobase.backend.enums.Bank;
import jakarta.persistence.Embeddable;
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

  private Bank bank;

  private String accountOwner;

  private String accountNumber;

}
