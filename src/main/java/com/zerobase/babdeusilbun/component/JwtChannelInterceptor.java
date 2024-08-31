package com.zerobase.babdeusilbun.component;

import static com.zerobase.babdeusilbun.exception.ErrorCode.AUTHENTICATION_HEADER_INVALID;
import static com.zerobase.babdeusilbun.exception.ErrorCode.JWT_TOKEN_EXPIRED;
import static com.zerobase.babdeusilbun.exception.ErrorCode.JWT_TOKEN_IS_BLACK;
import static com.zerobase.babdeusilbun.security.util.SecurityConstantsUtil.AUTHORIZATION_HEADER_NAME;
import static com.zerobase.babdeusilbun.security.util.SecurityConstantsUtil.AUTHORIZATION_HEADER_PREFIX;

import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.security.component.JwtComponent;
import com.zerobase.babdeusilbun.security.redis.RedisKeyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class JwtChannelInterceptor implements ChannelInterceptor {
  private final JwtComponent jwtComponent;
  private final RedisTemplate<String, String> redisTemplate;
  private final UserDetailsService userDetailsService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    if (!StompCommand.CONNECT.equals(accessor.getCommand())) {
      return message; //CONNECT가 아닌 경우 message 리턴
    }

    //CONNECT인 경우 사용자 정보 저장
    String authenticationHeader = accessor.getFirstNativeHeader(AUTHORIZATION_HEADER_NAME);
    if (!StringUtils.hasText(authenticationHeader) || !authenticationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
      throw new CustomException(AUTHENTICATION_HEADER_INVALID);
    }

    String jwtToken = authenticationHeader.replace(AUTHORIZATION_HEADER_PREFIX, "");
    // 해당 jwt token이 black list 상태인지 확인
    verifyJwtBlackList(jwtToken);

    // jwt token에서 email 가져옴
    String prefixedEmail = jwtComponent.getEmail(jwtToken);

    // jwt token이 만료된 상태인지 확인
    verifyJwtTokenIsExpired(jwtToken);

    UserDetails findUserDetails = userDetailsService.loadUserByUsername(prefixedEmail);

    Authentication authentication = new UsernamePasswordAuthenticationToken(
        findUserDetails, null, findUserDetails.getAuthorities()
    );
    accessor.setUser(authentication); //정보 저장

    return message;
  }

  private void verifyJwtBlackList(String jwtToken) {
    if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyUtil.jwtBlackListKey(jwtToken)))) {
      throw new CustomException(JWT_TOKEN_IS_BLACK);
    }
  }

  private void verifyJwtTokenIsExpired(String jwtToken) {
    if (jwtComponent.isExpired(jwtToken)) {
      throw new CustomException(JWT_TOKEN_EXPIRED);
    }
  }
}
