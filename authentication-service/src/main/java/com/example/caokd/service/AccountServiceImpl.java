package com.example.caokd.service;

import com.example.caokd.dto.UserPrinciple;
import com.example.caokd.entity.Account;
import com.example.caokd.repository.AccountRepo;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class AccountServiceImpl implements AccountService {

  @Autowired
  private AccountRepo accountRepo;

  @Override
  public Account create(Account account) {
    return accountRepo.save(account);
  }

  @Override
  public boolean existsByUserName(String userName) {
    return accountRepo.existsByUserName(userName);
  }

  @Override
  public boolean existsByEmail(String email) {
    return accountRepo.existsByEmail(email);
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Account account = accountRepo.findByUserName(username);

    return UserPrinciple.build(account);
  }
}
