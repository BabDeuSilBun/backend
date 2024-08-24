package com.zerobase.babdeusilbun.config;

import static com.zerobase.babdeusilbun.util.ChatUtility.SEND_TO_CLIENT_PREFIX;
import static com.zerobase.babdeusilbun.util.ChatUtility.SEND_TO_SERVER_PREFIX;
import static com.zerobase.babdeusilbun.util.ChatUtility.STOMP_PREFIX;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private final StompHandler stompHandler;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker(SEND_TO_CLIENT_PREFIX);
    config.setApplicationDestinationPrefixes(SEND_TO_SERVER_PREFIX);
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(STOMP_PREFIX).setAllowedOrigins("*")
        .addInterceptors(new HttpSessionHandshakeInterceptor());//.withSockJS();
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(stompHandler);
  }
}
