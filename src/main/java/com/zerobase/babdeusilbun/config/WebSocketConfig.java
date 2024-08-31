package com.zerobase.babdeusilbun.config;

import static com.zerobase.babdeusilbun.util.ChatUtility.SEND_TO_CLIENT_PREFIX;
import static com.zerobase.babdeusilbun.util.ChatUtility.SEND_TO_SERVER_PREFIX;
import static com.zerobase.babdeusilbun.util.ChatUtility.STOMP_PREFIX;

import com.zerobase.babdeusilbun.component.JwtChannelInterceptor;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import jakarta.annotation.Nullable;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private final JwtChannelInterceptor jwtChannelInterceptor;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker(SEND_TO_CLIENT_PREFIX);
    config.setApplicationDestinationPrefixes(SEND_TO_SERVER_PREFIX);
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(STOMP_PREFIX).setAllowedOriginPatterns("*");
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(jwtChannelInterceptor);
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(customUserDetailsWebsocketArgumentResolver());
  }

  @Bean
  HandlerMethodArgumentResolver customUserDetailsWebsocketArgumentResolver() {
    return new CustomUserDetailsWebsocketArgumentResolver();
  }

  static class CustomUserDetailsWebsocketArgumentResolver implements HandlerMethodArgumentResolver {
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
}
