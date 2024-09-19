package com.zerobase.babdeusilbun.security.service;

import com.zerobase.babdeusilbun.dto.SignDto.VerifyPasswordRequest;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyPasswordResponse;
import com.zerobase.babdeusilbun.security.dto.SignRequest.BusinessSignUp;
import com.zerobase.babdeusilbun.security.dto.SignRequest.SignIn;
import com.zerobase.babdeusilbun.security.dto.SignRequest.UserSignUp;
import com.zerobase.babdeusilbun.security.dto.WithdrawalRequest;
import com.zerobase.babdeusilbun.security.type.Role;

public interface SignService {
  VerifyPasswordResponse passwordConfirm(VerifyPasswordRequest request, Role role, Long userId);

  boolean isUserEmailIsUnique(String email);

  boolean isEntrepreneurEmailIsUnique(String email);

  void userSignUp(UserSignUp request);

  String userSignIn(SignIn request);

  String entrepreneurSignIn(SignIn request);

  void entrepreneurSignUp(BusinessSignUp request);

  void logout(String jwtToken);

  void userWithdrawal(String jwtToken, WithdrawalRequest request);

  void entrepreneurWithdrawal(String jwtToken, WithdrawalRequest request);
}
