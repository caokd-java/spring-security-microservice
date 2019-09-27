package com.example.caokd.filter1;


import com.example.caokd.dto.UserPrinciple;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    @Value("${caokd.app.jwtSecret}")
    private String jwtSecret;

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody();
  }

  public UserPrinciple getRoleFromToken(String token) {
    Claims body = Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody();

    Long id = 0L;
    String name = body.getSubject();
    String username = "";
    String email = "";
    String password = "";
    Collection<? extends GrantedAuthority> authorities = Arrays.stream(body.get("authorities").toString().split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    UserPrinciple userPrinciple = new UserPrinciple(id, name, username, email, password, authorities);

    return userPrinciple;
  }

  private Boolean isTokenNotExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.after(new Date());
  }

  public Optional<Boolean> validateToken(String token) {
    return  isTokenNotExpired(token) ? Optional.of(Boolean.TRUE) : Optional.empty();
  }

}
