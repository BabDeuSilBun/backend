package com.zerobase.backend.security.service;

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
import com.zerobase.backend.security.dto.SignResponse;
import com.zerobase.backend.security.exception.SecurityCustomException;
import com.zerobase.backend.security.exception.SecurityErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final EntrepreneurRepository entrepreneurRepository;
  private final SchoolRepository schoolRepository;
  private final MajorRepository majorRepository;

  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public boolean checkEmailIsDup(String email) {
    return !userRepository.existsByEmail(email) || !entrepreneurRepository.existsByEmail(email);
  }

  public void userSignup(UserSignUp request) {

    User createdUser = createNewUser(request);
    userRepository.save(createdUser);

  }

  public SignResponse signin(SignIn request) {
    return null;
  }

  public void businessSignup(BusinessSignUp request) {

    Entrepreneur createdEntrepreneur = createNewEntrepreneur(request);
    entrepreneurRepository.save(createdEntrepreneur);

  }

  private User createNewUser(UserSignUp request) {
    Long schoolId = request.getSchoolId();
    School findSchool = schoolRepository.findById(schoolId)
        .orElseThrow(() -> new SecurityCustomException(SecurityErrorCode.SCHOOL_NOT_FOUND));
    Major findMajor = majorRepository.findById(schoolId)
        .orElseThrow(() -> new SecurityCustomException(SecurityErrorCode.MAJOR_NOT_FOUND));

    return User.builder()
        .school(findSchool)
        .major(findMajor)
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .isBanned(false)
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
