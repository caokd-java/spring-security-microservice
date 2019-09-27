package com.example.caokd.controller;

import com.example.caokd.dto.JwtResponse;
import com.example.caokd.dto.LoginForm;
import com.example.caokd.dto.SignUpForm;
import com.example.caokd.entity.Account;
import com.example.caokd.entity.Role;
import com.example.caokd.entity.RoleName;
import com.example.caokd.filter.JwtProvider;
import com.example.caokd.service.AccountService;
import com.example.caokd.service.RoleService;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  AccountService accountService;

  @Autowired
  RoleService roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtProvider jwtProvider;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
    );

    String jwt = jwtProvider.generateJwtToken(authentication);
    return ResponseEntity.ok(new JwtResponse(jwt));
  }

  @PostMapping("/signup")
  public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
    if (accountService.existsByUserName(signUpRequest.getUsername())) {
      return new ResponseEntity<String>("Fail -> Username is already taken!",
          HttpStatus.BAD_REQUEST);
    }

    if (accountService.existsByEmail(signUpRequest.getEmail())) {
      return new ResponseEntity<String>("Fail -> Email is already in use!",
          HttpStatus.BAD_REQUEST);
    }

    // Creating user's account
    Account account = new Account(signUpRequest.getName(), signUpRequest.getUsername(),
        encoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail());

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    strRoles.forEach(role -> {
      switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN);
          roles.add(adminRole);

          break;
        case "pm":
          Role pmRole = roleRepository.findByName(RoleName.ROLE_PM);
          roles.add(pmRole);

          break;
        default:
          Role userRole = roleRepository.findByName(RoleName.ROLE_USER);
          roles.add(userRole);
      }
    });

    account.setRoles(roles);
    accountService.create(account);

    return ResponseEntity.ok().body("User registered successfully!");
  }
}
