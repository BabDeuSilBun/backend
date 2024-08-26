package com.zerobase.babdeusilbun.config;

import java.util.Objects;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

  @Value("${redis.host}")
//  @Value("${redis.host}")
  private String redisHost;

  @Value("${redis.port}")
//  @Value("${spring.data.redis.port}")
  private Integer redisPort;

//  @Value("${redis.username}")
//  @Value("${spring.data.redis.username}")
//  private String username;
//  @Value("${redis.password}")
//  @Value("${spring.data.redis.password}")
//  private String password;


  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(redisHost, redisPort);
  }

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    config.useSingleServer()
        .setAddress(getRedissonAddress());
//        .setUsername(username).setPassword(password);

    return Redisson.create(config);
  }

  private String getRedissonAddress() {
    return "redis://" + redisHost + ":" + redisPort;
  }

}
