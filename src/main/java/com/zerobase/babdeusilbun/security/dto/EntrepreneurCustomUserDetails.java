package com.zerobase.babdeusilbun.security.dto;

import static com.zerobase.babdeusilbun.security.type.Role.ROLE_ENTREPRENEUR;

import com.zerobase.babdeusilbun.domain.Entrepreneur;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class EntrepreneurCustomUserDetails implements UserDetails {

  private final Entrepreneur entrepreneur;

  public EntrepreneurCustomUserDetails(Entrepreneur entrepreneur) {
    this.entrepreneur = entrepreneur;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(ROLE_ENTREPRENEUR.name()));
  }

  @Override
  public String getPassword() {
    return entrepreneur.getPassword();
  }

  @Override
  public String getUsername() {
    return entrepreneur.getEmail();
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
