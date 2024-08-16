package com.zerobase.babdeusilbun.security.dto;

import static com.zerobase.babdeusilbun.security.type.Role.ROLE_ENTREPRENEUR;
import static com.zerobase.babdeusilbun.security.type.Role.ROLE_USER;

import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.security.type.Role;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {
  @Getter
  private final Long id;

  private final String email;

  private final Role role;

  public CustomUserDetails(User user) {
    id = user.getId();
    email = ROLE_USER + "_" + user.getEmail();
    role = ROLE_USER;
  }

  public CustomUserDetails(Entrepreneur entrepreneur) {
    id = entrepreneur.getId();
    email = ROLE_ENTREPRENEUR + "_" + entrepreneur.getEmail();
    role = ROLE_ENTREPRENEUR;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}