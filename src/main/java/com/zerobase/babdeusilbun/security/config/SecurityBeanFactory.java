package com.zerobase.babdeusilbun.security.config;

import io.jsonwebtoken.Jwts.SIG;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeanFactory {

  @Value("${jwt.secret-key}")
  private String secretKey;


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecretKey secretKey() {
    return new SecretKeySpec(
        secretKey.getBytes(StandardCharsets.UTF_8),
        SIG.HS256.key().build().getAlgorithm()
    );
  }



}
