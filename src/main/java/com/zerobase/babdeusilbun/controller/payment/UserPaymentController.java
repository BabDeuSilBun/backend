package com.zerobase.babdeusilbun.controller.payment;

import static com.zerobase.babdeusilbun.dto.SnapshotDto.PaymentSnapshot;
import static org.springframework.http.HttpStatus.OK;

import com.zerobase.babdeusilbun.dto.PaymentDto.ConfirmRequest;
import com.zerobase.babdeusilbun.dto.PaymentDto.ConfirmResponse;
import com.zerobase.babdeusilbun.dto.PaymentDto.ProcessRequest;
import com.zerobase.babdeusilbun.dto.PaymentDto.ProcessResponse;
import com.zerobase.babdeusilbun.dto.PaymentDto.Temporary;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import com.zerobase.babdeusilbun.service.PaymentService;
import com.zerobase.babdeusilbun.service.SnapshotService;
import com.zerobase.babdeusilbun.swagger.annotation.payment.UserPaymentSwagger.GetPaymentSnapshotsSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.payment.UserPaymentSwagger.PaymentConfirmSwagger;
import com.zerobase.babdeusilbun.swagger.annotation.payment.UserPaymentSwagger.PaymentProcessSwagger;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/meetings/{meetingId}")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserPaymentController {
  private final SnapshotService snapshotService;
  private final PaymentService paymentService;
  private final HttpSession httpSession;

  @GetMapping("/snapshots/payments")
  @GetPaymentSnapshotsSwagger
  public ResponseEntity<PaymentSnapshot> getPaymentSnapshots(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("meetingId") Long meetingId
  ) {

    return ResponseEntity.ok(PaymentSnapshot.fromPaymentEntity(
        snapshotService.getPaymentSnapshot(userDetails.getId(), meetingId)
    ));
  }

  @PostMapping("/purchases/{purchaseId}/payment")
  @PaymentProcessSwagger
  public ResponseEntity<ProcessResponse> paymentProcess(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable("meetingId") Long meetingId, @PathVariable("purchaseId") Long purchaseId,
      @RequestBody ProcessRequest request
  ) {

    ProcessResponse processResponse =
        paymentService.requestPayment(userDetails.getId(), meetingId, purchaseId, request);

    // session에 임시 저장
    httpSession.setAttribute("temporaryPayment", Temporary.fromDto(request, processResponse));

    return ResponseEntity.status(OK).body(processResponse);
  }

  @PostMapping("/purchases/{purchaseId}/payment/done")
  @PaymentConfirmSwagger
  public ResponseEntity<ConfirmResponse> paymentConfirm(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable("meetingId") Long meetingId, @PathVariable("purchaseId") Long purchaseId,
      @RequestBody ConfirmRequest request
  ) {

    Temporary temporary = (Temporary) httpSession.getAttribute("temporaryPayment");

    ConfirmResponse response = paymentService.confirmPayment
        (userDetails.getId(), meetingId, purchaseId, request, temporary);

    // session에서 제거
    httpSession.removeAttribute("temporaryPayment");

    return ResponseEntity.ok(response);
  }
}





