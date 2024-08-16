package com.zerobase.babdeusilbun.security.service.impl;

import static com.zerobase.babdeusilbun.exception.ErrorCode.EMAIL_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.USER_NOT_FOUND;

import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.security.dto.EntrepreneurCustomUserDetails;
import com.zerobase.babdeusilbun.security.dto.UserCustomUserDetails;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.security.type.Role;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;
  private final EntrepreneurRepository entrepreneurRepository;

  @Override
  public UserDetails loadUserByUsername(String prefixedEmail) throws UsernameNotFoundException {

    int splitIndex = prefixedEmail.indexOf("_", 5);
    String role = prefixedEmail.substring(0, splitIndex);
    String email = prefixedEmail.substring(splitIndex + 1);

    Role role1 = Role.valueOf(role);
    switch (role1) {
      case ROLE_USER -> {
        return new CustomUserDetails(findUserByEmail(email));
      }
      case ROLE_ENTREPRENEUR -> {
        return new CustomUserDetails(findEntrepreneurByEmail(email));
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
