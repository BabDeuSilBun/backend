package com.zerobase.babdeusilbun.security.filter;

import static com.zerobase.babdeusilbun.exception.ErrorCode.AUTHENTICATION_HEADER_INVALID;
import static com.zerobase.babdeusilbun.exception.ErrorCode.JWT_TOKEN_EXPIRED;
import static com.zerobase.babdeusilbun.exception.ErrorCode.JWT_TOKEN_IS_BLACK;
import static com.zerobase.babdeusilbun.security.constants.SecurityConstants.*;

import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.security.constants.SecurityConstants;
import com.zerobase.babdeusilbun.security.redis.RedisKeyUtil;
import com.zerobase.babdeusilbun.security.util.JwtComponent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFilter extends OncePerRequestFilter {

  private final JwtComponent jwtComponent;
  private final UserDetailsService userDetailsService;
  private final List<String> permitAllUrl;
  private final AntPathMatcher matcher = new AntPathMatcher();

  private final RedisTemplate<String, String> redisTemplate;

  public JwtFilter(JwtComponent jwtComponent, UserDetailsService userDetailsService,
      List<String> permitAllUrl, RedisTemplate<String, String> redisTemplate) {
    this.jwtComponent = jwtComponent;
    this.userDetailsService = userDetailsService;
    this.permitAllUrl = permitAllUrl;
    this.redisTemplate = redisTemplate;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // permitAll 속성을 지닌 url들은 filter 적용 안함
    for (String url : permitAllUrl) {
      if (matcher.match(url, request.getRequestURI())) {
        filterChain.doFilter(request, response);
        return;
      }
    }

    // header로부터 jwt token 가져옴
    String jwtToken = parsingJwtFromHeader(request);

    // 해당 jwt token이 black list 상태인지 확인
    verifyJwtBlackList(jwtToken);

    // jwt token에서 email 가져옴
    String email = jwtComponent.getEmail(jwtToken);

    // jwt token이 만료된 상태인지 확인
    verifyJwtTokenIsExpired(jwtToken);

    UserDetails findUserDetails = userDetailsService.loadUserByUsername(email);

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            findUserDetails, null, findUserDetails.getAuthorities()
        );

    setSecurityContext(authenticationToken);

    filterChain.doFilter(request, response);
  }

  private void verifyJwtBlackList(String jwtToken) {
    if (redisTemplate.hasKey(RedisKeyUtil.jwtBlackListKey(jwtToken))) {
      throw new CustomException(JWT_TOKEN_IS_BLACK);
    }
  }

  private void setSecurityContext(Authentication authentication) {
    SecurityContext securityContext = new SecurityContextImpl();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.getContextHolderStrategy().setContext(securityContext);
  }

  private String parsingJwtFromHeader(HttpServletRequest request) {
    String authenticationHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);

    verifyValidHeader(authenticationHeader);

    return authenticationHeader.replace(AUTHORIZATION_HEADER_PREFIX, "");
  }

  private void verifyJwtTokenIsExpired(String jwtToken) {
    if (jwtComponent.isExpired(jwtToken)) {
      throw new CustomException(JWT_TOKEN_EXPIRED);
    }
  }

  private void verifyValidHeader(String authenticationHeader) {
    if (!StringUtils.hasText(authenticationHeader) || !authenticationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
      throw new CustomException(AUTHENTICATION_HEADER_INVALID);
    }
  }
}
