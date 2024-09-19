package com.zerobase.babdeusilbun.swagger.enums;

import io.swagger.v3.oas.models.servers.Server;
import lombok.Getter;

@Getter
public enum SwaggerServer {
  LOCAL_HTTP_SERVER("http://localhost:8080", "request to http localhost"),
  LOCAL_HTTPS_SERVER("https://localhost:8080", "request to https localhost"),
  DEPLOYED_HTTPS_SERVER("http://3.34.19.176:8080", "request to deployed server");

  private final String url;
  private final String description;
  private final Server server;

  SwaggerServer(String url, String description) {
    this.url = url;
    this.description = description;
    this.server = new Server().url(url).description(description);
  }
}
