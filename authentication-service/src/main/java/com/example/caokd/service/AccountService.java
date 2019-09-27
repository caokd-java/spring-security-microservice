package com.example.caokd.service;

import com.example.caokd.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {

  Account create(Account account);

  boolean existsByUserName(String userName);

  boolean existsByEmail(String email);
}
