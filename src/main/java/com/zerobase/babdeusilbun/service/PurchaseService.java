package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.PurchaseDto.PurchaseResponse;
import org.springframework.data.domain.Pageable;

public interface PurchaseService {

  PurchaseResponse getTeamPurchaseCart(Long userId, Long meetingId, Pageable pageable);


  PurchaseResponse getIndividualPurchaseCart(Long userId, Long meetingId, Pageable pageable);

}
