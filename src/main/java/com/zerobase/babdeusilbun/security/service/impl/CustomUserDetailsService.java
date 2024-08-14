package com.zerobase.babdeusilbun.security.service.impl;

import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.USER_NOT_FOUND;

import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.security.dto.EntrepreneurCustomUserDetails;
import com.zerobase.babdeusilbun.security.dto.UserCustomUserDetails;
import com.zerobase.babdeusilbun.exception.CustomException;
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
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    boolean isUser = userRepository.existsByEmail(email);
    boolean isEntrepreneur = entrepreneurRepository.existsByEmail(email);

    if (isUser) {
      User findUser = findUserByEmail(email);
      return new CustomUserDetails(findUser);
    }
    if (isEntrepreneur) {
      Entrepreneur findEntrepreneur = findEntrepreneurByEmail(email);
      return new CustomUserDetails(findEntrepreneur);
    }

    return null;
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
