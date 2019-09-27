package com.example.caokd.config;

import com.example.caokd.filter1.JwtAuthenticationEntryPoint;
import com.example.caokd.filter1.JwtAuthenticationProvider;
import com.example.caokd.filter1.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthenticationEntryPoint unauthorizedHandler;

  @Autowired
  private JwtAuthenticationProvider jwtAuthenticationProvider;

  @Autowired
  public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) {
    authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider);
  }

  @Bean
  public JwtAuthenticationTokenFilter authenticationJwtTokenFilter() {
    return new JwtAuthenticationTokenFilter();
  }

  //=========================

//  @Bean
//  public JwtAuthenticationTokenFilter authenticationJwtTokenFilter() {
//    return new JwtAuthenticationTokenFilter();
//  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
        .antMatchers("/api/test/user").access("hasRole('ROLE_USER')")
        .antMatchers("/api/test/pm").access("hasRole('ROLE_PM')")
        .antMatchers("/api/test/admin").access("hasRole('ROLE_ADMIN')")
        .anyRequest().authenticated();

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  }
}
