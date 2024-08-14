package com.zerobase.babdeusilbun.security.dto;

import static com.zerobase.babdeusilbun.security.type.Role.ROLE_USER;

import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.security.type.Role;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserCustomUserDetails implements UserDetails {

  private final String email;

  private final Role role;

  public UserCustomUserDetails(User user) {
    email = user.getEmail();
    role = ROLE_USER;
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