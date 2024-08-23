package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.PaymentDto.ConfirmRequest;
import com.zerobase.babdeusilbun.dto.PaymentDto.ConfirmResponse;
import com.zerobase.babdeusilbun.dto.PaymentDto.ProcessRequest;
import com.zerobase.babdeusilbun.dto.PaymentDto.ProcessResponse;
import com.zerobase.babdeusilbun.dto.PaymentDto.Temporary;

public interface PaymentService {

  ProcessResponse requestPayment(Long userId, Long meetingId, Long purchaseId, ProcessRequest processRequest);

  ConfirmResponse confirmPayment(
      Long userId, Long meetingId, Long purchaseId,
      ConfirmRequest request, Temporary temporary
  );

}
