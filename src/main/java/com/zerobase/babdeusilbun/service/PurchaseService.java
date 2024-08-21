package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.PurchaseDto.DeliveryFeeResponse;
import com.zerobase.babdeusilbun.dto.PurchaseDto.PurchaseResponse;
import org.springframework.data.domain.Pageable;

public interface PurchaseService {

  PurchaseResponse getTeamPurchaseCart(Long meetingId, Pageable pageable);


  PurchaseResponse getIndividualPurchaseCart(Long userId, Long meetingId, Pageable pageable);

  DeliveryFeeResponse getDeliveryFeeInfo(Long userId, Long meetingId);
}
