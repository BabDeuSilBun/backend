package com.zerobase.babdeusilbun.security.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.zerobase.babdeusilbun.security.filter.JwtFilter;
import com.zerobase.babdeusilbun.security.component.JwtComponent;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtComponent jwtComponent;
  private final UserDetailsService userDetailsService;
  private final RedisTemplate<String, String> redisTemplate;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    List<String> permitAllUrls = Arrays.asList(
        "", "/", "/health-check", "/api/users/email-duplicated",
        "/api/businesses/email-duplicated",
        "/api/users/sign**", "/api/businesses/sign**", "/h2-console/**",
        "/swagger-ui/**", "/swagger-ui-custom.html", "/v3/api-docs/**",
        "/api/signup**", "/api/schools", "/api/stores/**"
    );

    http
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
    ;

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(permitAllUrls.toArray(new String[0])).permitAll()
            .anyRequest().authenticated()
        );

    http
        .addFilterBefore(jwtFilter(permitAllUrls), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  public JwtFilter jwtFilter(List<String> permitAllUrl) {
    return new JwtFilter(jwtComponent, userDetailsService, permitAllUrl, redisTemplate);
  }

  // cors 설정
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("http://localhost:3000");
    configuration.addAllowedMethod(GET);
    configuration.addAllowedMethod(POST);
    configuration.addAllowedMethod(PUT);
    configuration.addAllowedMethod(PATCH);
    configuration.addAllowedMethod(DELETE);
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
  public WebSecurityCustomizer configureH2ConsoleEnable() {
    return web -> web.ignoring()
        .requestMatchers(PathRequest.toH2Console())
        .requestMatchers("/", "/swagger-ui/**", "/swagger-ui-custom.html", "/v3/api-docs/**");
  }




  /**
   * 테스트용 계정
   */
//  @Bean
  public UserDetailsService userDetailsService() {

    UserDetails admin = User.withUsername("admin").password("{noop}1234").roles("ADMIN").build();
    UserDetails user = User.withUsername("user").password("{noop}1234").roles("USER").build();
    return new InMemoryUserDetailsManager(admin, user);
  }
}
