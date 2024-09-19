package com.zerobase.babdeusilbun.security.service.impl;

import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_ORDER_PROCEEDING;
import static com.zerobase.babdeusilbun.exception.ErrorCode.ENTREPRENEUR_WITHDRAWAL;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MAJOR_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.PASSWORD_NOT_MATCH;
import static com.zerobase.babdeusilbun.exception.ErrorCode.SCHOOL_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.USER_MEETING_STILL_LEFT;
import static com.zerobase.babdeusilbun.exception.ErrorCode.USER_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.USER_POINT_NOT_EMPTY;
import static com.zerobase.babdeusilbun.exception.ErrorCode.USER_WITHDRAWAL;
import static com.zerobase.babdeusilbun.security.redis.RedisKeyUtil.jwtBlackListKey;
import static com.zerobase.babdeusilbun.security.redis.RedisKeyUtil.refreshTokenKey;
import static com.zerobase.babdeusilbun.security.type.Role.ROLE_ENTREPRENEUR;
import static com.zerobase.babdeusilbun.security.type.Role.ROLE_USER;
import static com.zerobase.babdeusilbun.security.util.SecurityConstantsUtil.getPrefixedEmail;
import static com.zerobase.babdeusilbun.util.NicknameUtil.createRandomNickname;

import com.zerobase.babdeusilbun.domain.Address;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Major;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.School;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyPasswordRequest;
import com.zerobase.babdeusilbun.dto.SignDto.VerifyPasswordResponse;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.MajorRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.component.JwtComponent;
import com.zerobase.babdeusilbun.security.dto.SignRequest;
import com.zerobase.babdeusilbun.security.dto.SignRequest.BusinessSignUp;
import com.zerobase.babdeusilbun.security.dto.SignRequest.SignIn;
import com.zerobase.babdeusilbun.security.dto.SignRequest.UserSignUp;
import com.zerobase.babdeusilbun.security.dto.WithdrawalRequest;
import com.zerobase.babdeusilbun.security.service.SignService;
import com.zerobase.babdeusilbun.security.type.Role;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Transactional
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

  @Value("${jwt.expire-ms}")
  private Long jwtTokenExpiredMs;

  private final UserRepository userRepository;
  private final EntrepreneurRepository entrepreneurRepository;
  private final SchoolRepository schoolRepository;
  private final MajorRepository majorRepository;
  private final PurchaseRepository purchaseRepository;
  private final MeetingRepository meetingRepository;

  private final JwtComponent jwtComponent;
  private final PasswordEncoder passwordEncoder;

  private final RedisTemplate<String, String> stringRedisTemplate;
  private final RedisTemplate<String, String> refreshTokenRedisTemplate;

  private final JavaMailSender mailSender;
  private final StringRedisTemplate redisTemplate;
  private final SpringTemplateEngine templateEngine;

  /**
   * 비밀번호 확인
   */
  @Override
  @Transactional(readOnly = true)
  public VerifyPasswordResponse passwordConfirm(VerifyPasswordRequest request, Role role, Long userId) {
    String encodePassword = null;

    switch(role) {
      case ROLE_USER -> encodePassword = userRepository.findByIdAndDeletedAtIsNull(userId)
          .orElseThrow(() -> new CustomException(USER_NOT_FOUND))
          .getPassword();
      case ROLE_ENTREPRENEUR -> encodePassword = entrepreneurRepository.findByIdAndDeletedAtIsNull(userId)
          .orElseThrow(() -> new CustomException(ENTREPRENEUR_NOT_FOUND))
          .getPassword();
    }

    return VerifyPasswordResponse.builder()
        .isCorrected(passwordEncoder.matches(request.getPassword(), encodePassword))
        .build();
  }

  /**
   * 사용자 이메일 중복 확인
   */
  @Override
  @Transactional(readOnly = true)
  public boolean isUserEmailIsUnique(String email) {
    return !userRepository.existsByEmail(email);
  }

  /**
   * 사업자 이메일 중복 확인
   */
  @Override
  @Transactional(readOnly = true)
  public boolean isEntrepreneurEmailIsUnique(String email) {
    return !entrepreneurRepository.existsByEmail(email);
  }

  /**
   * 사용자 회원가입
   */
  @Override
  public void userSignUp(UserSignUp request) {

    User createdUser = createNewUser(request);
    userRepository.save(createdUser);

  }

  /**
   * 사용자 로그인
   */
  @Override
  @Transactional(readOnly = true)
  public String userSignIn(SignIn request) {

    String email = request.getEmail();
    String password = request.getPassword();

    User findUser = findUserByEmail(email);

    // 탈퇴한 회원인지 확인
    verifyWithdrawalUser(findUser);

    // 비밀번호가 올바른지 검증
    verifyPassword(password, findUser.getPassword());

    Role role = ROLE_USER;
    return jwtComponent.createToken(getPrefixedEmail(email, role), role.name());
  }

  /**
   * 사업자 회원가입
   */
  @Override
  public void entrepreneurSignUp(BusinessSignUp request) {

    Entrepreneur createdEntrepreneur = createNewEntrepreneur(request);
    entrepreneurRepository.save(createdEntrepreneur);
  }

  /**
   * 사업자 로그인
   */
  @Override
  @Transactional(readOnly = true)
  public String entrepreneurSignIn(SignIn request) {

    String email = request.getEmail();
    String password = request.getPassword();

    Entrepreneur findEntrepreneur = findEntrepreneurByEmail(email);
    // 탈퇴한 회원인지 확인
    verifyWithdrawalEntrepreneur(findEntrepreneur);
    verifyPassword(password, findEntrepreneur.getPassword());

    Role role = ROLE_ENTREPRENEUR;
    return jwtComponent.createToken(getPrefixedEmail(email, role), role.name());
  }

  /**
   * 로그아웃
   */
  @Override
  public void logout(String jwtToken) {

    String email = jwtComponent.getEmail(jwtToken);

    // redis에 해당 jwt를 balcklist로 등록
    stringRedisTemplate.opsForValue()
        .set(jwtBlackListKey(jwtToken), email, jwtTokenExpiredMs, TimeUnit.MILLISECONDS);

    // redis에서 refresh token 정보 삭제
    refreshTokenRedisTemplate.delete(refreshTokenKey(email));

    SecurityContextHolder.getContextHolderStrategy().clearContext();
  }

  /**
   * 사용자 회원탈퇴
   */
  @Override
  public void userWithdrawal(String jwtToken, WithdrawalRequest request) {

    String emailByToken = jwtComponent.getEmail(jwtToken);
    User findUser = findUserByEmail(emailByToken);

    // 이미 탈퇴한 회원인지 확인
    verifyWithdrawalUser(findUser);

    verifyPassword(request.getPassword(), findUser.getPassword());

    // 잔여 포인트가 존재하는지 확인
    verifyLeftPoint(findUser);

    // 진행중인 모임이 존재하는지 확인
    verifyProceedingMeeting(findUser);

    findUser.withdraw();

    // 로그아웃 처리
    logout(jwtToken);
  }

  /**
   * 사업자 회원탈퇴
   */
  @Override
  public void entrepreneurWithdrawal(String jwtToken, WithdrawalRequest request) {

    String emailByToken = jwtComponent.getEmail(jwtToken);
    Entrepreneur findEntrepreneur = findEntrepreneurByEmail(emailByToken);

    verifyPassword(request.getPassword(), findEntrepreneur.getPassword());

    // 이미 탈퇴한 회원인지 확인
    verifyWithdrawalEntrepreneur(findEntrepreneur);

    // 주문을 접수, 진행 중인 가게가 있다면 탈퇴 불가
    verifyProceedingPurchase(findEntrepreneur);

    findEntrepreneur.withdraw();

    // 로그아웃 처리
    logout(jwtToken);
  }

  private void verifyLeftPoint(User findUser) {
    if (findUser.getPoint() > 0) {
      throw new CustomException(USER_POINT_NOT_EMPTY);
    }
  }


  private void verifyPassword(String rawPassword, String encodedPassword) {
    if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
      throw new CustomException(PASSWORD_NOT_MATCH);
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

  private User createNewUser(UserSignUp request) {
    Long schoolId = request.getSchoolId();
    School findSchool = schoolRepository.findById(schoolId)
        .orElseThrow(() -> new CustomException(SCHOOL_NOT_FOUND));
    Major findMajor = majorRepository.findById(schoolId)
        .orElseThrow(() -> new CustomException(MAJOR_NOT_FOUND));

    return User.builder()
        .school(findSchool)
        .major(findMajor)
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .isBanned(false)
        .nickname(createRandomNickname())
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

  private void verifyWithdrawalUser(User findUser) {
    if (findUser.getDeletedAt() != null) {
      throw new CustomException(USER_WITHDRAWAL);
    }
  }

  private void verifyWithdrawalEntrepreneur(Entrepreneur findEntrepreneur) {
    if (findEntrepreneur.getDeletedAt() != null) {
      throw new CustomException(ENTREPRENEUR_WITHDRAWAL);
    }
  }

  private void verifyProceedingMeeting(User findUser) {
    List<Meeting> leaderMeetings = meetingRepository.findProceedingByLeader(findUser);
    List<Meeting> userMeetings = meetingRepository.findProceedingByParticipant(findUser);
    if (!leaderMeetings.isEmpty() || !userMeetings.isEmpty()) {
      throw new CustomException(USER_MEETING_STILL_LEFT);
    }
  }

  private void verifyProceedingPurchase(Entrepreneur findEntrepreneur) {
    List<Purchase> proceedingPurchase = purchaseRepository.findProceedingByOwner(findEntrepreneur);
    if (!proceedingPurchase.isEmpty()) {
      throw new CustomException(ENTREPRENEUR_ORDER_PROCEEDING);
    }
  }


}
