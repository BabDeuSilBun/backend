package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.PurchaseDto.TeamPurchaseResponse;
import org.springframework.data.domain.Pageable;

public interface PurchaseService {

  TeamPurchaseResponse getTeamOrderCart(Long userId, Long meetingId, Pageable pageable);
}
