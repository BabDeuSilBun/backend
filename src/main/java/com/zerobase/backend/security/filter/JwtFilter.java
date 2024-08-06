package com.zerobase.backend.security.filter;

import static com.zerobase.backend.security.exception.SecurityErrorCode.AUTHENTICATION_HEADER_INVALID;
import static com.zerobase.backend.security.exception.SecurityErrorCode.JWT_TOKEN_EXPIRED;
import static com.zerobase.backend.security.exception.SecurityErrorCode.JWT_TOKEN_INVALID;

import com.zerobase.backend.security.exception.SecurityCustomException;
import com.zerobase.backend.security.exception.SecurityErrorCode;
import com.zerobase.backend.security.util.JwtComponent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFilter extends OncePerRequestFilter {

  private final JwtComponent jwtComponent;
  private final UserDetailsService userDetailsService;
  private final List<String> permitAllUrl;
  private final AntPathMatcher matcher = new AntPathMatcher();

  public JwtFilter(JwtComponent jwtComponent, UserDetailsService userDetailsService,
      List<String> permitAllUrl) {
    this.jwtComponent = jwtComponent;
    this.userDetailsService = userDetailsService;
    this.permitAllUrl = permitAllUrl;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    for (String url : permitAllUrl) {
      if (matcher.match(url, request.getRequestURI())) {
        filterChain.doFilter(request, response);
        return;
      }
    }

    String jwtToken = parsingJwtFromHeader(request);
    String email = jwtComponent.getEmail(jwtToken);

    verifyJwtTokenIsExpired(jwtToken);

    Authentication authentication =
        SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();

    UserDetails findUserDetails = userDetailsService.loadUserByUsername(email);

    UserDetails userDetailsFromContext = (UserDetails) authentication.getPrincipal();
    String emailFromSecurityContext = userDetailsFromContext.getUsername();

    if (!email.equals(emailFromSecurityContext)) {
      throw new SecurityCustomException(JWT_TOKEN_INVALID);
    }
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            findUserDetails, null, findUserDetails.getAuthorities()
        );

    setSecurityContext(authenticationToken);
  }

  private void setSecurityContext(UsernamePasswordAuthenticationToken authenticationToken) {
    SecurityContext securityContext = new SecurityContextImpl();
    securityContext.setAuthentication(authenticationToken);
    SecurityContextHolder.getContextHolderStrategy().setContext(securityContext);
  }

  private String parsingJwtFromHeader(HttpServletRequest request) {
    String authenticationHeader = request.getHeader("Authentication");

    if (!StringUtils.hasText(authenticationHeader) || !authenticationHeader.startsWith("Bearer ")) {
      throw new SecurityCustomException(AUTHENTICATION_HEADER_INVALID);
    }

    return authenticationHeader.replace("Bearer ", "");
  }

  private void verifyJwtTokenIsExpired(String jwtToken) {
    if (jwtComponent.isExpired(jwtToken)) {
      throw new SecurityCustomException(JWT_TOKEN_EXPIRED);
    }
  }
}
