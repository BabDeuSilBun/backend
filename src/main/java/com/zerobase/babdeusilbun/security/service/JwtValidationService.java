package com.zerobase.babdeusilbun.security.service;

public interface JwtValidationService {

  String verifyJwtFromHeader(String authorizationHeader);

}
