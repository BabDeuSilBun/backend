package com.zerobase.babdeusilbun.security.config;

import static com.zerobase.babdeusilbun.util.ChatUtility.SEND_TO_CLIENT_PREFIX;
import static com.zerobase.babdeusilbun.util.ChatUtility.SEND_TO_SERVER_PREFIX;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class SecurityWebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
  @Override
  protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
    message
        .nullDestMatcher().permitAll()
        .simpDestMatchers(SEND_TO_SERVER_PREFIX + "/**").authenticated()
        .simpSubscribeDestMatchers(SEND_TO_CLIENT_PREFIX + "/**").authenticated()
        .anyMessage().denyAll();
  }

  @Override
  protected boolean sameOriginDisabled() {
    return true;
  }
}
