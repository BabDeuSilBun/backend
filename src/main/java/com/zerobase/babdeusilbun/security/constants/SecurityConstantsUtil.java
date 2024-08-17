package com.zerobase.babdeusilbun.security.constants;

import static com.zerobase.babdeusilbun.exception.ErrorCode.*;
import static com.zerobase.babdeusilbun.security.type.Role.*;

import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.security.type.Role;

public class SecurityConstantsUtil {

  public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

  public static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

  public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

  public static final String USER_EMAIL_PREFIX = ROLE_USER.name() + ":";

  public static final String ENTREPRENEUR_EMAIL_PREFIX = ROLE_ENTREPRENEUR.name() + ":";



  public static String getPrefixedEmail(String email, Role role) {
    if (role == ROLE_USER) {
      return USER_EMAIL_PREFIX + email;
    } else if (role == ROLE_ENTREPRENEUR) {
      return ENTREPRENEUR_EMAIL_PREFIX + email;
    } else {
      return "unknown:" + email;
    }
  }

  public static String getOriginalEmail(String prefixedEmail) {
    if (isUser(prefixedEmail)) {
      return prefixedEmail.replace(USER_EMAIL_PREFIX, "");
    } else if (isEntrepreneur(prefixedEmail)) {
      return prefixedEmail.replace(ENTREPRENEUR_EMAIL_PREFIX, "");
    } else {
      throw new CustomException(EMAIL_PREFIX_INVALID);
    }
  }

  public static Role getRoleFromPrefixedEmail(String prefixedEmail) {
    if (isUser(prefixedEmail)) {
      return ROLE_USER;
    } else if (isEntrepreneur(prefixedEmail)) {
      return ROLE_ENTREPRENEUR;
    } else {
      throw new CustomException(EMAIL_PREFIX_INVALID);
    }
  }

  private static boolean isUser(String prefixedEmail) {
    return prefixedEmail.startsWith(USER_EMAIL_PREFIX);
  }

  private static boolean isEntrepreneur(String prefixedEmail) {
    return prefixedEmail.startsWith(ENTREPRENEUR_EMAIL_PREFIX);
  }

}
