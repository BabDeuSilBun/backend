package com.zerobase.babdeusilbun.security.filter;

import static com.zerobase.babdeusilbun.exception.ErrorCode.AUTHENTICATION_HEADER_INVALID;
import static com.zerobase.babdeusilbun.exception.ErrorCode.JWT_TOKEN_EXPIRED;
import static com.zerobase.babdeusilbun.exception.ErrorCode.JWT_TOKEN_IS_BLACK;
import static com.zerobase.babdeusilbun.security.util.SecurityConstantsUtil.*;
import static java.time.format.DateTimeFormatter.*;

import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.security.redis.RedisKeyUtil;
import com.zerobase.babdeusilbun.security.component.JwtComponent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

    log.info("[{}][{}][{}][enter jwt filter]", Thread.currentThread().getName(), LocalDateTime.now(
        ZoneId.of("Asia/Seoul")).format(ISO_LOCAL_DATE_TIME), request.getRequestURI());

    // permitAll 속성을 지닌 url들은 filter 적용 안함
    for (String url : permitAllUrl) {
      if (matcher.match(url, request.getRequestURI())) {
        log.info("[{}][{}][{}][pass jwt filter]", Thread.currentThread().getName(), LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(ISO_LOCAL_DATE_TIME), request.getRequestURI());
        filterChain.doFilter(request, response);
        return;
      }
    }

    // header로부터 jwt token 가져옴
    String jwtToken = parsingJwtFromHeader(request);

    // 해당 jwt token이 black list 상태인지 확인
    verifyJwtBlackList(jwtToken);

    // jwt token에서 email 가져옴
    String prefixedEmail = jwtComponent.getEmail(jwtToken);

    // jwt token이 만료된 상태인지 확인
    verifyJwtTokenIsExpired(jwtToken);

    UserDetails findUserDetails = userDetailsService.loadUserByUsername(prefixedEmail);

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            findUserDetails, null, findUserDetails.getAuthorities()
        );

    setSecurityContext(authenticationToken);

    log.info("[{}][{}][{}][success auth jwt filter]", Thread.currentThread().getName(), LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(ISO_LOCAL_DATE_TIME), request.getRequestURI());

    filterChain.doFilter(request, response);
  }

  private void verifyJwtBlackList(String jwtToken) {
    if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyUtil.jwtBlackListKey(jwtToken)))) {
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

    // 유효한 Authorization 헤더인지 검증
    verifyValidHeader(authenticationHeader);

    // jwt token만 추출
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
