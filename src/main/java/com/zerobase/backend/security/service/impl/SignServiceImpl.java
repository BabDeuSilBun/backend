package com.zerobase.backend.security.service.impl;

import static com.zerobase.backend.security.exception.SecurityErrorCode.*;
import static com.zerobase.backend.security.redis.RedisKeyUtil.*;
import static com.zerobase.backend.security.type.Role.*;

import com.zerobase.backend.domain.Address;
import com.zerobase.backend.domain.Entrepreneur;
import com.zerobase.backend.domain.Major;
import com.zerobase.backend.domain.School;
import com.zerobase.backend.domain.User;
import com.zerobase.backend.repository.EntrepreneurRepository;
import com.zerobase.backend.repository.MajorRepository;
import com.zerobase.backend.repository.SchoolRepository;
import com.zerobase.backend.repository.UserRepository;
import com.zerobase.backend.security.dto.SignRequest;
import com.zerobase.backend.security.dto.SignRequest.*;
import com.zerobase.backend.security.exception.SecurityCustomException;
import com.zerobase.backend.security.redis.RedisKeyUtil;
import com.zerobase.backend.security.service.SignService;
import com.zerobase.backend.security.util.JwtComponent;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

  @Value("${jwt.expire-ms}")
  private Long jwtTokenExpiredMs;

  private final UserRepository userRepository;
  private final EntrepreneurRepository entrepreneurRepository;
  private final SchoolRepository schoolRepository;
  private final MajorRepository majorRepository;

  private final JwtComponent jwtComponent;
  private final PasswordEncoder passwordEncoder;

  private final RedisTemplate<String, String> stringRedisTemplate;
  private final RedisTemplate<String, String> refreshTokenRedisTemplate;

  /**
   * 이메일 중복 확인
   */
  @Override
  public boolean isEmailIsUnique(String email) {
    return !userRepository.existsByEmail(email) && !entrepreneurRepository.existsByEmail(email);
  }

  @Override
  @Transactional
  public void userSignUp(UserSignUp request) {

    User createdUser = createNewUser(request);
    userRepository.save(createdUser);

  }

  @Override
  public String userSignIn(SignIn request) {

    String email = request.getEmail();
    String password = request.getPassword();

    User findUser = findUserByEmail(email);
    verifyPassword(password, findUser.getPassword());

    return jwtComponent.createToken(email, ROLE_USER.name());
  }

  @Override
  @Transactional
  public void entrepreneurSignUp(BusinessSignUp request) {

    Entrepreneur createdEntrepreneur = createNewEntrepreneur(request);
    entrepreneurRepository.save(createdEntrepreneur);

  }

  @Override
  public String entrepreneurSignIn(SignIn request) {

    String email = request.getEmail();
    String password = request.getPassword();

    Entrepreneur findEntrepreneur = findEntrepreneurByEmail(email);
    verifyPassword(password, findEntrepreneur.getPassword());

    return jwtComponent.createToken(email, ROLE_ENTREPRENEUR.name());
  }

  @Override
  public void logout(String jwtToken) {

    String email = jwtComponent.getEmail(jwtToken);

    stringRedisTemplate.opsForValue().set(jwtBlackListKey(jwtToken), email, jwtTokenExpiredMs, TimeUnit.MILLISECONDS);
    refreshTokenRedisTemplate.delete(refreshTokenKey(email));

    SecurityContextHolder.getContextHolderStrategy().clearContext();
  }


  private void verifyPassword(String rawPassword, String encodedPassword) {
    if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
      throw new SecurityCustomException(PASSWORD_NOT_MATCH);
    }
  }

  private Entrepreneur findEntrepreneurByEmail(String email) {
    return entrepreneurRepository.findByEmail(email)
        .orElseThrow(() -> new SecurityCustomException(ENTREPRENEUR_NOT_FOUND));
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new SecurityCustomException(USER_NOT_FOUND));
  }

  private User createNewUser(UserSignUp request) {
    Long schoolId = request.getSchoolId();
    School findSchool = schoolRepository.findById(schoolId)
        .orElseThrow(() -> new SecurityCustomException(SCHOOL_NOT_FOUND));
    Major findMajor = majorRepository.findById(schoolId)
        .orElseThrow(() -> new SecurityCustomException(MAJOR_NOT_FOUND));

    return User.builder()
        .school(findSchool)
        .major(findMajor)
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .isBanned(false)
        //TODO
        // 닉네임 생성 유틸 필요
        .nickname("testnickname")
        .name(request.getName())
        .phoneNumber(request.getPhoneNumber())
        .point(0L)
        .address(getAddressFromRequest(request.getAddress()))
        .build();
  }

  private Entrepreneur createNewEntrepreneur(BusinessSignUp request) {
    return Entrepreneur.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .name(request.getName())
        .phoneNumber(request.getPhoneNumber())
        .businessNumber(request.getBusinessNumber())
        .address(getAddressFromRequest(request.getAddress()))
        .build();
  }

  private Address getAddressFromRequest(SignRequest.Address requestAddress) {
    return Address.builder()
        .postal(requestAddress.getPostal())
        .streetAddress(requestAddress.getStreetAddress())
        .detailAddress(requestAddress.getDetailAddress())
        .build();
  }
}
