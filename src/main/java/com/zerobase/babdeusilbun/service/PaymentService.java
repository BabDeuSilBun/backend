package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.IamPortDto.Request;
import com.zerobase.babdeusilbun.dto.IamPortDto.Response;

public interface PaymentService {

  Response confirmParticipant(Long userId, Long meetingId, Long purchaseId, Request request);

}
