package com.zerobase.babdeusilbun.websocket.component;

import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import jakarta.annotation.Nullable;
import java.security.Principal;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsWebsocketArgumentResolver implements HandlerMethodArgumentResolver {
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return CustomUserDetails.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  @Nullable
  public Object resolveArgument(MethodParameter parameter, Message<?> message) {
    Principal principal = SimpMessageHeaderAccessor.getUser(message.getHeaders());

    if (principal instanceof AbstractAuthenticationToken abstractAuthenticationToken) {
      return abstractAuthenticationToken.getPrincipal();
    }

    throw new CustomException(ErrorCode.USER_NOT_FOUND);
  }
}
