package com.example.caokd.filter;


import com.example.caokd.dto.UserPrinciple;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

  @Value("${caokd.app.jwtSecret}")
  private String jwtSecret;

  @Value("${caokd.app.jwtExpiration}")
  private int jwtExpiration; // 1h

  public String generateJwtToken(Authentication authentication) {

    UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

    String authorities = userPrincipal.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .claim("authorities", authorities)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }
}
