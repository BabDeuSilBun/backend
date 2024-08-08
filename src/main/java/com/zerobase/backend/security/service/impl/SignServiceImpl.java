package com.zerobase.backend.security.service.impl;

import static com.zerobase.backend.security.exception.SecurityErrorCode.ENTREPRENEUR_NOT_FOUND;
import static com.zerobase.backend.security.exception.SecurityErrorCode.ENTREPRENEUR_ORDER_PROCEEDING;
import static com.zerobase.backend.security.exception.SecurityErrorCode.ENTREPRENEUR_WITHDRAWAL;
import static com.zerobase.backend.security.exception.SecurityErrorCode.MAJOR_NOT_FOUND;
import static com.zerobase.backend.security.exception.SecurityErrorCode.PASSWORD_NOT_MATCH;
import static com.zerobase.backend.security.exception.SecurityErrorCode.SCHOOL_NOT_FOUND;
import static com.zerobase.backend.security.exception.SecurityErrorCode.USER_MEETING_STILL_LEFT;
import static com.zerobase.backend.security.exception.SecurityErrorCode.USER_NOT_FOUND;
import static com.zerobase.backend.security.exception.SecurityErrorCode.USER_POINT_NOT_EMPTY;
import static com.zerobase.backend.security.exception.SecurityErrorCode.USER_WITHDRAWAL;
import static com.zerobase.backend.security.redis.RedisKeyUtil.jwtBlackListKey;
import static com.zerobase.backend.security.redis.RedisKeyUtil.refreshTokenKey;
import static com.zerobase.backend.security.type.Role.ROLE_ENTREPRENEUR;
import static com.zerobase.backend.security.type.Role.ROLE_USER;

import com.zerobase.backend.domain.Address;
import com.zerobase.backend.domain.Entrepreneur;
import com.zerobase.backend.domain.Major;
import com.zerobase.backend.domain.School;
import com.zerobase.backend.domain.User;
import com.zerobase.backend.repository.EntrepreneurRepository;
import com.zerobase.backend.repository.MajorRepository;
import com.zerobase.backend.repository.MeetingRepository;
import com.zerobase.backend.repository.PurchaseRepository;
import com.zerobase.backend.repository.SchoolRepository;
import com.zerobase.backend.repository.TeamPurchaseRepository;
import com.zerobase.backend.repository.UserRepository;
import com.zerobase.backend.security.dto.SignRequest;
import com.zerobase.backend.security.dto.SignRequest.BusinessSignUp;
import com.zerobase.backend.security.dto.SignRequest.SignIn;
import com.zerobase.backend.security.dto.SignRequest.UserSignUp;
import com.zerobase.backend.security.dto.WithdrawalRequest;
import com.zerobase.backend.security.exception.SecurityCustomException;
import com.zerobase.backend.security.service.SignService;
import com.zerobase.backend.security.util.JwtComponent;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

  private final PurchaseRepository purchaseRepository;
  private final TeamPurchaseRepository teamPurchaseRepository;
  @Value("${jwt.expire-ms}")
  private Long jwtTokenExpiredMs;

  private final UserRepository userRepository;
  private final EntrepreneurRepository entrepreneurRepository;
  private final SchoolRepository schoolRepository;
  private final MajorRepository majorRepository;

  private final MeetingRepository meetingRepository;

  private final JwtComponent jwtComponent;
  private final PasswordEncoder passwordEncoder;

  private final RedisTemplate<String, String> stringRedisTemplate;
  private final RedisTemplate<String, String> refreshTokenRedisTemplate;

  /**
   * 이메일 중복 확인
   */
  @Override
  @Transactional(readOnly = true)
  public boolean isEmailIsUnique(String email) {
    return !userRepository.existsByEmail(email) && !entrepreneurRepository.existsByEmail(email);
  }

  @Override
  public void userSignUp(UserSignUp request) {

    User createdUser = createNewUser(request);
    userRepository.save(createdUser);

  }

  @Override
  @Transactional(readOnly = true)
  public String userSignIn(SignIn request) {

    String email = request.getEmail();
    String password = request.getPassword();

    User findUser = findUserByEmail(email);

    // 회원 탈퇴한 유저인지 확인
    verifyUserWithdrawal(findUser);

    verifyPassword(password, findUser.getPassword());

    return jwtComponent.createToken(email, ROLE_USER.name());
  }

  @Override
  public void entrepreneurSignUp(BusinessSignUp request) {

    Entrepreneur createdEntrepreneur = createNewEntrepreneur(request);
    entrepreneurRepository.save(createdEntrepreneur);
  }

  @Override
  @Transactional(readOnly = true)
  public String entrepreneurSignIn(SignIn request) {

    String email = request.getEmail();
    String password = request.getPassword();

    Entrepreneur findEntrepreneur = findEntrepreneurByEmail(email);
    // 회원 탈퇴한 사업가 인지 확인
    verifyWithdrawalEntrepreneur(findEntrepreneur);
    verifyPassword(password, findEntrepreneur.getPassword());

    return jwtComponent.createToken(email, ROLE_ENTREPRENEUR.name());
  }

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

  @Override
  public void userWithdrawal(UserDetails userDetails, WithdrawalRequest request) {
    // 회원의 비밀번호 재확인 후 회원탈퇴 진행, 진행 중인 모임, 잔여 유효 포인트 존재 시 탈퇴 불가

    String emailByUserDetails = userDetails.getUsername();
    User findUser = findUserByEmail(emailByUserDetails);

    verifyPassword(request.getPassword(), findUser.getPassword());

    // 잔여 포인트가 존재하는지 확인
    verifyLeftPoint(findUser);

    // 진행중인 모임이 존재하는지 확인
    meetingRepository.findAllByLeader(findUser)
            .forEach(m -> {
              if (m.getStatus().isProgress()) {
                throw new SecurityCustomException(USER_MEETING_STILL_LEFT);
              }
            });

    meetingRepository.findAllByParticipant(findUser)
        .forEach(m -> {
          if (m.getStatus().isProgress()) {
            throw new SecurityCustomException(USER_MEETING_STILL_LEFT);
          }
        });

    findUser.withdraw();
  }

  @Override
  public void entrepreneurWithdrawal(UserDetails userDetails, WithdrawalRequest request) {

    String emailByUserDetails = userDetails.getUsername();
    Entrepreneur findEntrepreneur = findEntrepreneurByEmail(emailByUserDetails);

    verifyPassword(request.getPassword(), findEntrepreneur.getPassword());

    // 주문을 접수, 진행 중인 가게가 있다면 탈퇴 불가
    // 개별주문
    purchaseRepository.findAllByEntrepreneur(findEntrepreneur)
        .forEach(p -> {
          if (p.getStatus().isProceeding()) {
            throw new SecurityCustomException(ENTREPRENEUR_ORDER_PROCEEDING);
          }
        });

    // 공동주문
    teamPurchaseRepository.findAllByEntrepreneur(findEntrepreneur)
        .forEach(tp -> {
          if (tp.getStatus().isProceeding()) {
            throw new SecurityCustomException(ENTREPRENEUR_ORDER_PROCEEDING);
          }
        });

    findEntrepreneur.withdraw();
  }

  private void verifyLeftPoint(User findUser) {
    if (findUser.getPoint() > 0) {
      throw new SecurityCustomException(USER_POINT_NOT_EMPTY);
    }
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

  private void verifyUserWithdrawal(User findUser) {
    if (findUser.getDeletedAt() != null) {
      throw new SecurityCustomException(USER_WITHDRAWAL);
    }
  }

  private void verifyWithdrawalEntrepreneur(Entrepreneur findEntrepreneur) {
    if (findEntrepreneur.getDeletedAt() != null) {
      throw new SecurityCustomException(ENTREPRENEUR_WITHDRAWAL);
    }
  }
}
