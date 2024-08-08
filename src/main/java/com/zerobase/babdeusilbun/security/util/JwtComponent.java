package com.zerobase.babdeusilbun.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtComponent {

  private final SecretKey secretKey;

  @Value("${jwt.expire-ms}")
  private String expireMs;

  private final UserDetailsService userDetailsService;

  /**
   * Jwt Token 생성
   */
  public String createToken(String email, String role) {

    return Jwts.builder().signWith(secretKey)
        .claim("email", email)
        .claim("role", role)
        .expiration(new Date(System.currentTimeMillis() + Long.parseLong(expireMs)))
        .compact();
  }

  /**
   * Jwt Token에서 email 파싱
   */
  public String getEmail(String token) {
    return getClaims(token).get("email", String.class);
  }

  /**
   * Jwt Token에서 role 파싱
   */
  public String getRole(String token) {
    return getClaims(token).get("role", String.class);
  }

  /**
   * Jwt Token 만료 검증
   */
  public boolean isExpired(String token) {
    return getClaims(token).getExpiration().before(new Date());
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(getEmail(token));
    return new UsernamePasswordAuthenticationToken
        (userDetails, "", userDetails.getAuthorities());
  }

  /**
   * Jwt Token에서 claims 파싱
   */
  private Claims getClaims(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
  }

}
