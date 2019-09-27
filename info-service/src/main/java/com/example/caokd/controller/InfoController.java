package com.example.caokd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

  @GetMapping("/api/test/user")
  public String userAccess() {
    return ">>> User Contents!";
  }

  @GetMapping("/api/test/pm")
  public String projectManagementAccess() {
    return ">>> Board Management Project";
  }

  @GetMapping("/api/test/admin")
  public String adminAccess() {
    return ">>> Admin Contents";
  }
}
