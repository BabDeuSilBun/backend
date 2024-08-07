package com.zerobase.backend.security.application;

import static com.zerobase.backend.security.exception.SecurityErrorCode.EMAIL_NOT_FOUND;
import static com.zerobase.backend.security.exception.SecurityErrorCode.JWT_AND_REFRESH_TOKEN_NOT_MATCH;
import static com.zerobase.backend.security.exception.SecurityErrorCode.REFRESH_TOKEN_INVALID;
import static com.zerobase.backend.security.type.Role.*;

import com.zerobase.backend.repository.EntrepreneurRepository;
import com.zerobase.backend.repository.UserRepository;
import com.zerobase.backend.security.dto.RefreshToken;
import com.zerobase.backend.security.dto.SignRequest.SignIn;
import com.zerobase.backend.security.dto.SignResponse;
import com.zerobase.backend.security.exception.SecurityCustomException;
import com.zerobase.backend.security.service.RefreshTokenService;
import com.zerobase.backend.security.service.SignService;
import com.zerobase.backend.security.type.Role;
import com.zerobase.backend.security.util.JwtComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthApplication {

  private final SignService signService;
  private final RefreshTokenService refreshTokenService;
  private final UserDetailsService userDetailsService;

  private final UserRepository userRepository;
  private final EntrepreneurRepository entrepreneurRepository;

  private final JwtComponent jwtComponent;


  /**
   * 로그인
   * 유저, 사업자 공용
   */
  public SignResponse signin(SignIn request) {

    String email = request.getEmail();
    String jwtToken = null;

    // 해당 이메일이 유저 이메일인지, 사업자 이메일인지 확인
    boolean isUser = userRepository.existsByEmail(email);
    boolean isEntrepreneur = entrepreneurRepository.existsByEmail(email);

    // 해당 이메일의 계정 정보가 DB에 없을경우 예외
    verifyExistAuthentication(isUser, isEntrepreneur);

    // 해당 이메일의 사용자가 유저인경우
    if(isUser) {
      // 로그인 처리 후 jwt token 발행
      jwtToken = signService.userSignIn(request);
    }
    // 해당 이메일의 사용자가 사업자인경우
    if (isEntrepreneur) {
      // 로그인 처리 후 jwt token 발행
      jwtToken = signService.entrepreneurSignIn(request);
    }

    // 해당 이메일의 refresh token 발행
    String refreshToken = refreshTokenService.createRefreshToken(jwtToken, email);

    // 해당 계정의 Authentication 객체를 SecurityContext에 저장
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    setAuthenticationToSecurityContext(userDetails);

    return SignResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
  }

  /**
   * refresh token 이용하여 jwt token, refresh token 재발급
   */
  public SignResponse reGenerateToken(String curJwtToken, String curRefreshToken) {
    // token 유효성 검증
    verifyMatch(curJwtToken, curRefreshToken);

    // jwt로부터 email 추출
    String emailFromJwt = jwtComponent.getEmail(curJwtToken);
    // 추출한 email로 redis에서 refresh token 가져옴
    RefreshToken refreshTokenByEmail = refreshTokenService.findRefreshTokenByEmail(emailFromJwt);

    // refresh token이 만료되었는지 확인
    verifyRefreshTokenExpiration(refreshTokenByEmail);

    // refresh token에서 email 추출
    String emailFromRefresh = refreshTokenByEmail.getEmail();

    // 추출한 email로 refresh token 재발급
    String newRefreshToken = refreshTokenService.createRefreshToken(curJwtToken, emailFromRefresh);

    // 기존의 refresh token 정보를 redis에서 삭제
//    refreshTokenService.deleteCurRefreshToken(curRefreshToken);

    // jwt token 재발급 시작
    // 현재 사용자가 유저인지 사업자인지 확인
    boolean isUser = userRepository.existsByEmail(emailFromRefresh);
    boolean isEntrepreneur = entrepreneurRepository.existsByEmail(emailFromRefresh);
    Role role = null;

    // 해당 이메일의 계정 정보가 DB에 없을경우 예외
    verifyExistAuthentication(isUser, isEntrepreneur);

    // 해당 이메일의 사용자가 유저인경우 role 값을 유저로 할당
    if (isUser) role = ROLE_USER;
    // 해당 이메일의 사용자가 사업자인경우 role 값을 사업자로 할당
    if (isEntrepreneur) role = ROLE_ENTREPRENEUR;

    // 새로운 jwt token 발행
    String newJwtToken = jwtComponent.createToken(emailFromRefresh, role.name());

    return SignResponse.builder().accessToken(newJwtToken).refreshToken(newRefreshToken).build();
  }

  private void setAuthenticationToSecurityContext(UserDetails userDetails) {
    Authentication authentication = new UsernamePasswordAuthenticationToken
        (userDetails, "", userDetails.getAuthorities());
    SecurityContextHolder.getContextHolderStrategy().setContext(new SecurityContextImpl(authentication));
  }

  private void verifyExistAuthentication(boolean isUser, boolean isEntrepreneur) {
    if (!isUser && !isEntrepreneur) {
      throw new SecurityCustomException(EMAIL_NOT_FOUND);
    }
  }

  private void verifyMatch(String curJwtToken, String curRefreshToken) {

    // 현재 jwt token과 refresh token이 올바른 짝인지 확인
    if (!refreshTokenService.tokenIsMatch(curJwtToken, curRefreshToken)) {
      throw new SecurityCustomException(JWT_AND_REFRESH_TOKEN_NOT_MATCH);
    }
  }

  private void verifyRefreshTokenExpiration(RefreshToken refreshToken) {
    // 현재 refresh token이 만료된 상태인지 확인
    if (refreshTokenService.isExpiredRefreshToken(refreshToken)) {
      throw new SecurityCustomException(REFRESH_TOKEN_INVALID);
    }
  }
}
