package com.zerobase.babdeusilbun.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElbController {
  @GetMapping("/health-check")
  public String healthCheck() {
    return "OK";
  }
}
