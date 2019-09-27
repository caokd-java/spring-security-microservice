package com.example.caokd.filter1;

import com.example.caokd.dto.UserPrinciple;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

  @Autowired
  private JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    final String requestHeader = request.getHeader("Authorization");

    if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
      String authToken = requestHeader.substring(7);
      JwtAuthentication authentication = new JwtAuthentication(authToken);

//      JwtParser jwtParser = Jwts.parser().setSigningKey("jwtCaoKDSecretKey");
//
//      Jws claimsJws = jwtParser.parseClaimsJws(authToken);
//      Claims claims = (Claims) claimsJws.getBody();

      UserPrinciple userPrinciple = jwtProvider.getRoleFromToken(authToken);

      UsernamePasswordAuthenticationToken authentication1
          = new UsernamePasswordAuthenticationToken(null, userPrinciple, userPrinciple.getAuthorities());
      authentication1.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(authentication1);
    }
    filterChain.doFilter(request, response);

  }
}
