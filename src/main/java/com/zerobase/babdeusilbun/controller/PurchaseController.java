package com.zerobase.babdeusilbun.controller;

import com.zerobase.babdeusilbun.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/meetings/{meetingId}")
@RequiredArgsConstructor
public class PurchaseController {

  private final PurchaseService purchaseService;

}
