package com.example.caokd.repository;

import com.example.caokd.entity.Role;
import com.example.caokd.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {

  Role findByName(RoleName roleName);
}
