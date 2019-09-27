package com.example.caokd.repository;

import com.example.caokd.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long> {

  Account findByUserName(String userName);

  boolean existsByUserName(String userName);

  boolean existsByEmail(String email);
}
