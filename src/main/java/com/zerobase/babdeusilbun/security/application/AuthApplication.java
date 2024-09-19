package com.zerobase.babdeusilbun.security.application;

import static com.zerobase.babdeusilbun.exception.ErrorCode.REFRESH_TOKEN_COOKIE_NOT_FOUND;
import static com.zerobase.babdeusilbun.exception.ErrorCode.REFRESH_TOKEN_INVALID;
import static com.zerobase.babdeusilbun.security.util.SecurityConstantsUtil.*;
import static com.zerobase.babdeusilbun.security.util.SecurityConstantsUtil.REFRESH_TOKEN_COOKIE;
import static com.zerobase.babdeusilbun.security.type.Role.ROLE_ENTREPRENEUR;
import static com.zerobase.babdeusilbun.security.type.Role.ROLE_USER;

import com.zerobase.babdeusilbun.security.dto.RefreshToken;
import com.zerobase.babdeusilbun.security.dto.SignRequest.SignIn;
import com.zerobase.babdeusilbun.security.dto.SignResponse;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.security.dto.WithdrawalRequest;
import com.zerobase.babdeusilbun.security.service.RefreshTokenService;
import com.zerobase.babdeusilbun.security.service.SignService;
import com.zerobase.babdeusilbun.security.type.Role;
import com.zerobase.babdeusilbun.security.component.JwtComponent;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
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

  private final JwtComponent jwtComponent;

  /**
   * 사용자 로그인
   */
  public SignResponse userSignin(SignIn request, HttpServletResponse servletResponse) {
    String email = request.getEmail();
    String prefixedEmail = getPrefixedEmail(email, ROLE_USER);

    // 로그인 처리 후 jwt token 발행
    String jwtToken = signService.userSignIn(request);

    // 해당 이메일의 refresh token 발행
    String refreshToken = refreshTokenService.createRefreshToken(prefixedEmail);

    // 해당 계정의 Authentication 객체를 SecurityContext에 저장
    UserDetails userDetails = userDetailsService.loadUserByUsername(prefixedEmail);
    setAuthenticationToSecurityContext(userDetails);

    // Refresh token을 cookie에 저장
    setRefreshTokenCookie(servletResponse, refreshToken);

    return SignResponse.builder().accessToken(jwtToken).build();
  }

  /**
   * 사업자 로그인
   */
  public SignResponse entrepreneurSignin(SignIn request, HttpServletResponse servletResponse) {
    String email = request.getEmail();
    String prefixedEmail = getPrefixedEmail(email, ROLE_ENTREPRENEUR);

    // 로그인 처리 후 jwt token 발행
    String jwtToken = signService.entrepreneurSignIn(request);

    // 해당 이메일의 refresh token 발행
    String refreshToken = refreshTokenService.createRefreshToken(prefixedEmail);

    // 해당 계정의 Authentication 객체를 SecurityContext에 저장
    UserDetails userDetails = userDetailsService.loadUserByUsername(prefixedEmail);
    setAuthenticationToSecurityContext(userDetails);

    // Refresh token을 cookie에 저장
    setRefreshTokenCookie(servletResponse, refreshToken);

    return SignResponse.builder().accessToken(jwtToken).build();
  }

  /**
   * 로그아웃
   */
  public void logout(String jwtToken, HttpServletResponse servletResponse) {
    signService.logout(jwtToken);

    // refresh token cookie 삭제
    deleteRefreshTokenCookie(servletResponse);
  }

  /**
   * 사용자 회원 탈퇴
   */
  public void userWithdrawal
      (String jwtToken, WithdrawalRequest request, HttpServletResponse servletResponse) {

    signService.userWithdrawal(jwtToken, request);

    // refresh token cookie 삭제
    deleteRefreshTokenCookie(servletResponse);
  }

  /**
   * 사업자 회원탈퇴
   */
  public void entrepreneurWithdrawal
      (String jwtToken, WithdrawalRequest request, HttpServletResponse servletResponse) {

    signService.entrepreneurWithdrawal(jwtToken, request);

    // refresh token cookie 삭제
    deleteRefreshTokenCookie(servletResponse);
  }

  /**
   * refresh token 이용하여 jwt token, refresh token 재발급
   */
  public SignResponse reGenerateToken
  (HttpServletRequest servletRequest, HttpServletResponse servletResponse) {

    // cookie에서 refresh token 가져옴
    String curRefreshToken = getRefreshTokenFromCookie(servletRequest);

    // 추출한 email로 redis에서 refresh token 가져옴
    RefreshToken refreshTokenDtoByToken = refreshTokenService.findDtoByRefreshToken(curRefreshToken);

    // refresh token이 만료되었는지 확인
    verifyRefreshTokenExpiration(refreshTokenDtoByToken);

    // refresh token에서 email 추출
    String prefixedEmailFromRefresh = refreshTokenDtoByToken.getEmail();

    // 추출한 email로 refresh token 재발급
    String newRefreshToken = refreshTokenService.createRefreshToken(prefixedEmailFromRefresh);

    // 기존의 refresh token을 redis에서 제거
    refreshTokenService.deleteRefreshTokenFromRedis(curRefreshToken);

    // jwt token 재발급 시작
    // 현재 사용자가 유저인지 사업자인지 확인
    Role role = getRoleFromPrefixedEmail(prefixedEmailFromRefresh);

    // 새로운 jwt token 발행
    String newJwtToken = jwtComponent.createToken(prefixedEmailFromRefresh, role.name());

    // 새로운 refresh token을 cookie에 저장
    setRefreshTokenCookie(servletResponse, newRefreshToken);

    return SignResponse.builder().accessToken(newJwtToken).build();
  }

  private void setAuthenticationToSecurityContext(UserDetails userDetails) {
    Authentication authentication = new UsernamePasswordAuthenticationToken
        (userDetails, "", userDetails.getAuthorities());
    SecurityContextHolder.getContextHolderStrategy()
        .setContext(new SecurityContextImpl(authentication));
  }

  private void verifyRefreshTokenExpiration(RefreshToken refreshToken) {
    // 현재 refresh token이 만료된 상태인지 확인
    if (refreshTokenService.isExpiredRefreshToken(refreshToken)) {
      throw new CustomException(REFRESH_TOKEN_INVALID);
    }
  }

  public void setRefreshTokenCookie(HttpServletResponse servletResponse, String refreshToken) {
    // Refresh Token을 HttpOnly 쿠키로 설정
    Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE, refreshToken);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(true); // HTTPS에서만 전송되도록 설정
    refreshTokenCookie.setPath("/"); // 쿠키가 유효한 경로
    refreshTokenCookie.setMaxAge(24 * 60 * 60); // 쿠키 유효기간: 1일

    // 쿠키를 응답에 추가
    servletResponse.addCookie(refreshTokenCookie);
  }

  public void deleteRefreshTokenCookie(HttpServletResponse servletResponse) {
    Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE, null);
    refreshTokenCookie.setHttpOnly(true);
    refreshTokenCookie.setSecure(true); // HTTPS에서만 사용
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge(0); // 즉시 만료되도록 설정

    // 쿠키를 응답에 추가 (쿠키가 즉시 만료되므로 삭제됨)
    servletResponse.addCookie(refreshTokenCookie);
  }

  private String getRefreshTokenFromCookie(HttpServletRequest servletRequest) {
    return Arrays.stream(servletRequest.getCookies())
        .filter(cookie -> REFRESH_TOKEN_COOKIE.equals(cookie.getName()))
        .findFirst()
        .map(Cookie::getValue)
        .orElseThrow(() -> new CustomException(REFRESH_TOKEN_COOKIE_NOT_FOUND));
  }
}
