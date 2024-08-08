package com.zerobase.backend.security.service;

public interface JwtValidationService {

  String verifyJwtFromHeader(String authorizationHeader);

}
