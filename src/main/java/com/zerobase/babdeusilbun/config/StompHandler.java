package com.zerobase.babdeusilbun.config;

import com.zerobase.babdeusilbun.security.component.JwtComponent;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
  private final JwtComponent jwtComponent;
  private final UserDetailsService userDetailsService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    System.out.println("StompHandler preSend called with command: " + accessor.getCommand());

    if (accessor.getCommand() == StompCommand.CONNECT) {
      validateToken(accessor);
    }

    return message;
  }

  private void validateToken(StompHeaderAccessor accessor) {
    String authorizationHeader = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
    System.out.println("authorizationHeader = " + authorizationHeader);

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      String jwtToken = authorizationHeader.substring(7);

      // jwt token에서 email 가져옴
      String prefixedEmail = jwtComponent.getEmail(jwtToken);

      UserDetails findUserDetails = userDetailsService.loadUserByUsername(prefixedEmail);

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
              findUserDetails, null, findUserDetails.getAuthorities()
          );

      accessor.setUser(authentication);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } else {
      throw new IllegalArgumentException("Authorization header is missing or invalid");
    }
  }
}
