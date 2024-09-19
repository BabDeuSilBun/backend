package com.zerobase.babdeusilbun.websocket.config;

import static com.zerobase.babdeusilbun.util.ChatUtility.SEND_TO_CLIENT_PREFIX;
import static com.zerobase.babdeusilbun.util.ChatUtility.SEND_TO_SERVER_PREFIX;
import static com.zerobase.babdeusilbun.util.ChatUtility.STOMP_PREFIX;

import com.zerobase.babdeusilbun.websocket.component.CustomUserDetailsWebsocketArgumentResolver;
import com.zerobase.babdeusilbun.websocket.component.JwtChannelInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private final JwtChannelInterceptor jwtChannelInterceptor;
  private final CustomUserDetailsWebsocketArgumentResolver customUserDetailsWebsocketArgumentResolver;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker(SEND_TO_CLIENT_PREFIX);
    config.setApplicationDestinationPrefixes(SEND_TO_SERVER_PREFIX);
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(STOMP_PREFIX).setAllowedOriginPatterns(
        "https://bdsb-frontend.vercel.app", "http://localhost:3000").withSockJS();
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(jwtChannelInterceptor);
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(customUserDetailsWebsocketArgumentResolver);
  }
}
