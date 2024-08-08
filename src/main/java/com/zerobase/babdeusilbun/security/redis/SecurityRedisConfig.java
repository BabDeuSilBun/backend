package com.zerobase.babdeusilbun.security.redis;

import com.zerobase.babdeusilbun.security.dto.RefreshToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class SecurityRedisConfig {

  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;


  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(redisHost, redisPort);
  }

  @Bean
  public RedisTemplate<String, RefreshToken> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, RefreshToken> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
  }

}
