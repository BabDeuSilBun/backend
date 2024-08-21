package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.PaymentDto.Request;
import com.zerobase.babdeusilbun.dto.PaymentDto.Response;

public interface PaymentService {

  Response confirmParticipant(Long userId, Long meetingId, Long purchaseId, Request request);

}
