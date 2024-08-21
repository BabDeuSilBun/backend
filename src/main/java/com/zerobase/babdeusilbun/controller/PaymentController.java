package com.zerobase.babdeusilbun.controller;

import static com.zerobase.babdeusilbun.dto.PaymentDto.*;
import static org.springframework.http.HttpStatus.OK;

import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  private final HttpSession httpSession;


  /**
   * 모임장, 모임원의 결제 진행 요청
   */
  @PreAuthorize("hasRole('USER')")
  @PostMapping("/api/users/meetings/{meetingId}/purchases/{purchaseId}/payment")
  public ResponseEntity<Response> paymentProcess(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long meetingId, @PathVariable Long purchaseId, @RequestBody Request request
  ) {

    Response response =
        paymentService.requestPayment(userDetails.getId(), meetingId, purchaseId, request);

    // session에 임시 저장
    httpSession.setAttribute("temporaryPayment", Temporary.fromDto(request, response));

    return ResponseEntity.status(OK).body(response);
  }

}
