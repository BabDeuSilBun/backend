package com.zerobase.babdeusilbun.security.service.impl;

import static com.zerobase.babdeusilbun.exception.ErrorCode.JWT_TOKEN_INVALID;

import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.security.service.JwtValidationService;
import com.zerobase.babdeusilbun.security.util.JwtComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtValidationServiceImpl implements JwtValidationService {

  private final JwtComponent jwtComponent;

  @Override
  @Transactional(readOnly = true)
  public String verifyJwtFromHeader(String authorizationHeader) {

    String jwtToken = authorizationHeader.replace("Bearer ", "");
    String emailByToken = jwtComponent.getEmail(jwtToken);
    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContextHolderStrategy()
        .getContext()
        .getAuthentication()
        .getPrincipal();

    String email = userDetails.getUsername();

    if (!emailByToken.equals(email)) {
      throw new CustomException(JWT_TOKEN_INVALID);
    }

    return jwtToken;
  }


}
