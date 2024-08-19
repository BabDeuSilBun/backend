package com.zerobase.babdeusilbun.security.service.impl;

import static com.zerobase.babdeusilbun.exception.ErrorCode.EMAIL_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.USER_NOT_FOUND;
import static com.zerobase.babdeusilbun.security.util.SecurityConstantsUtil.*;

import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.security.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;
  private final EntrepreneurRepository entrepreneurRepository;

  @Override
  public UserDetails loadUserByUsername(String prefixedEmail) throws UsernameNotFoundException {

//    int splitIndex = prefixedEmail.indexOf("_", 5);
//    String role = prefixedEmail.substring(0, splitIndex);
//    String email = prefixedEmail.substring(splitIndex + 1);
//
//    Role role1 = Role.valueOf(role);

    String originalEmail = getOriginalEmail(prefixedEmail);
    Role role = getRoleFromPrefixedEmail(prefixedEmail);

    switch (role) {
      case ROLE_USER -> {
        return new CustomUserDetails(findUserByEmail(originalEmail));
      }
      case ROLE_ENTREPRENEUR -> {
        return new CustomUserDetails(findEntrepreneurByEmail(originalEmail));
      }
      default -> throw new CustomException(EMAIL_NOT_FOUND);
    }

  }

  private Entrepreneur findEntrepreneurByEmail(String email) {
    return entrepreneurRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND));
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }
}
