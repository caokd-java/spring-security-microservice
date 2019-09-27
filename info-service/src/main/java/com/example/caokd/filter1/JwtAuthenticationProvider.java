package com.example.caokd.filter1;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private JwtProvider jwtProvider;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    try {
      String token = (String) authentication.getCredentials();
      String username = jwtProvider.getUsernameFromToken(token);

      return jwtProvider.validateToken(token)
          .map(aBoolean -> new JwtAuthenticatedProfile(username))
          .orElseThrow(() -> new JwtAuthenticationException("JWT Token validation failed"));

    } catch (JwtException ex) {
      throw new JwtAuthenticationException("Failed to verify token");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthentication.class.equals(authentication);
  }
}
