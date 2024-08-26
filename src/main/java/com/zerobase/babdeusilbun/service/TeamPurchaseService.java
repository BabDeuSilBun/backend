package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.domain.TeamPurchase;
import com.zerobase.babdeusilbun.dto.TeamPurchaseDto.CreateRequest;
import com.zerobase.babdeusilbun.dto.TeamPurchaseDto.UpdateRequest;

public interface TeamPurchaseService {
    TeamPurchase createIndividualPurchase(Long userId, Long meetingId, CreateRequest request);

    TeamPurchase updateIndividualPurchase(Long userId, Long purchaseId, UpdateRequest request);

    void deleteIndividualPurchase(Long userId, Long purchaseId);
}
