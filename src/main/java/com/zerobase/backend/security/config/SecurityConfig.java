package com.zerobase.backend.security.config;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.*;

import com.zerobase.backend.security.filter.JwtFilter;
import com.zerobase.backend.security.util.JwtComponent;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtComponent jwtComponent;
  private final UserDetailsService userDetailsService;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    List<String> permitAllUrls = Arrays.asList(
        "/", "/api/signin", "/api/user/signup", "/api/business/signup", "/h2-console/**"
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
    return new JwtFilter(jwtComponent, userDetailsService, permitAllUrl);
  }

  // cors 설정
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("http://localhost:3000");
    configuration.addAllowedMethod(GET);
    configuration.addAllowedMethod(POST);
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  @ConditionalOnProperty(name = "spring.h2.console.enabled",havingValue = "true")
  public WebSecurityCustomizer configureH2ConsoleEnable() {
    return web -> web.ignoring()
        .requestMatchers(PathRequest.toH2Console());
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
