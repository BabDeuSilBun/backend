package com.zerobase.babdeusilbun.security.application;

import static com.zerobase.babdeusilbun.exception.ErrorCode.EMAIL_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.JWT_AND_REFRESH_TOKEN_NOT_MATCH;
import static com.zerobase.babdeusilbun.exception.ErrorCode.REFRESH_TOKEN_INVALID;
import static com.zerobase.babdeusilbun.security.type.Role.ROLE_ENTREPRENEUR;
import static com.zerobase.babdeusilbun.security.type.Role.ROLE_USER;

import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.dto.RefreshToken;
import com.zerobase.babdeusilbun.security.dto.SignRequest;
import com.zerobase.babdeusilbun.security.dto.SignRequest.SignIn;
import com.zerobase.babdeusilbun.security.dto.SignResponse;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.security.service.RefreshTokenService;
import com.zerobase.babdeusilbun.security.service.SignService;
import com.zerobase.babdeusilbun.security.type.Role;
import com.zerobase.babdeusilbun.security.util.JwtComponent;
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

  public SignResponse userSignin(SignIn request) {
    String email = request.getEmail();
    String prefixedEmail = ROLE_USER.name() + "_" + email;

    // 로그인 처리 후 jwt token 발행
    String jwtToken = signService.userSignIn(request);

    // 해당 이메일의 refresh token 발행
    String refreshToken = refreshTokenService.createRefreshToken(jwtToken, prefixedEmail);

    // 해당 계정의 Authentication 객체를 SecurityContext에 저장
    UserDetails userDetails = userDetailsService.loadUserByUsername(prefixedEmail);
    setAuthenticationToSecurityContext(userDetails);

    return SignResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
  }

  public SignResponse businessSignin(SignIn request) {
    String email = request.getEmail();
    String prefixedEmail = ROLE_ENTREPRENEUR.name() + "_" + email;

    // 로그인 처리 후 jwt token 발행
    String jwtToken = signService.entrepreneurSignIn(request);

    // 해당 이메일의 refresh token 발행
    String refreshToken = refreshTokenService.createRefreshToken(jwtToken, prefixedEmail);

    // 해당 계정의 Authentication 객체를 SecurityContext에 저장
    UserDetails userDetails = userDetailsService.loadUserByUsername(prefixedEmail);
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

//    Role role;
    // jwt token 재발급 시작
    // 현재 사용자가 유저인지 사업자인지 확인
    // 해당 이메일의 사용자가 유저인경우 role 값을 유저로 할당
//    if (userRepository.existsByEmail(emailFromRefresh)) {
//      role = ROLE_USER;
//    }
//    // 해당 이메일의 사용자가 사업자인경우 role 값을 사업자로 할당
//    else if (entrepreneurRepository.existsByEmail(emailFromRefresh)) {
//      role = ROLE_ENTREPRENEUR;
//    }
//    // 해당 이메일의 계정 정보가 DB에 없을경우 예외
//    else {
//      throw new CustomException(EMAIL_NOT_FOUND);
//    }

    int splitIndex = emailFromRefresh.indexOf("_", 5);
    String role = emailFromRefresh.substring(0, splitIndex);
    String originalEmail = emailFromRefresh.substring(splitIndex + 1);

    // 새로운 jwt token 발행
    String newJwtToken = jwtComponent.createToken(originalEmail, role);

    return SignResponse.builder().accessToken(newJwtToken).refreshToken(newRefreshToken).build();
  }

  private void setAuthenticationToSecurityContext(UserDetails userDetails) {
    Authentication authentication = new UsernamePasswordAuthenticationToken
        (userDetails, "", userDetails.getAuthorities());
    SecurityContextHolder.getContextHolderStrategy()
        .setContext(new SecurityContextImpl(authentication));
  }

  private void verifyMatch(String curJwtToken, String curRefreshToken) {

    // 현재 jwt token과 refresh token이 올바른 짝인지 확인
    if (!refreshTokenService.tokenIsMatch(curJwtToken, curRefreshToken)) {
      throw new CustomException(JWT_AND_REFRESH_TOKEN_NOT_MATCH);
    }
  }

  private void verifyRefreshTokenExpiration(RefreshToken refreshToken) {
    // 현재 refresh token이 만료된 상태인지 확인
    if (refreshTokenService.isExpiredRefreshToken(refreshToken)) {
      throw new CustomException(REFRESH_TOKEN_INVALID);
    }
  }
}
