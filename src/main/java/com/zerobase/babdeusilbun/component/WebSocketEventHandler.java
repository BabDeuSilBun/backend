package com.zerobase.babdeusilbun.component;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Slf4j
@Component
public class WebSocketEventHandler {

  @EventListener
  public void handleWebSocketSessionConnect(SessionConnectEvent event) {
    logConnectEvent(event);
  }

  private void logConnectEvent(SessionConnectEvent event) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);

    Authentication authentication = (Authentication) Objects.requireNonNull(accessor.getUser());
    String username = authentication.getName();
    String userRole = authentication.getAuthorities()
        .stream()
        .findFirst().map(GrantedAuthority::getAuthority)
        .orElseThrow(() -> new IllegalArgumentException("User role is not exist."));

    log.info("WebSocket {}: username={}, userRole={}", event.getClass().getSimpleName(), username, userRole);
  }

  @EventListener(SessionConnectedEvent.class)
  public void handleWebSocketSessionConnected() {
    log.info("WebSocket Connected");
  }

  @EventListener(SessionDisconnectEvent.class)
  public void handleWebSocketSessionDisconnected() {
    log.info("WebSocket Disconnected");
  }

  @EventListener(SessionSubscribeEvent.class)
  public void handleWebSocketSessionSubscribe() {
    log.info("WebSocket Subscribe");
  }

  @EventListener(SessionUnsubscribeEvent.class)
  public void handleWebSocketSessionUnsubscribe() {
    log.info("WebSocket Unsubscribe");
  }
}
