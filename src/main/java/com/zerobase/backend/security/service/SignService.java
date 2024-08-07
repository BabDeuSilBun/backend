package com.zerobase.backend.security.service;

import com.zerobase.backend.security.dto.SignRequest.BusinessSignUp;
import com.zerobase.backend.security.dto.SignRequest.SignIn;
import com.zerobase.backend.security.dto.SignRequest.UserSignUp;

public interface SignService {

  boolean isEmailIsUnique(String email);

  void userSignUp(UserSignUp request);

  String userSignIn(SignIn request);

  String entrepreneurSignIn(SignIn request);

  void entrepreneurSignUp(BusinessSignUp request);

}
