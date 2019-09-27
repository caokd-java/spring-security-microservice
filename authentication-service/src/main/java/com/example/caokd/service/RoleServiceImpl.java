package com.example.caokd.service;

import com.example.caokd.entity.Role;
import com.example.caokd.entity.RoleName;
import com.example.caokd.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepo roleRepo;

  @Override
  public Role findByName(RoleName roleName) {
    return roleRepo.findByName(roleName);
  }
}
