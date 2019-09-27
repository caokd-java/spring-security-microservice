package com.example.caokd.service;

import com.example.caokd.entity.Role;
import com.example.caokd.entity.RoleName;

public interface RoleService {

  Role findByName(RoleName roleName);
}
