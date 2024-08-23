package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.dto.IndividualPurchaseDto.CreateRequest;
import com.zerobase.babdeusilbun.dto.IndividualPurchaseDto.UpdateRequest;
import com.zerobase.babdeusilbun.domain.IndividualPurchase;

public interface IndividualPurchaseService {
    IndividualPurchase createIndividualPurchase(Long userId, Long meetingId, CreateRequest request);

    IndividualPurchase updateIndividualPurchase(Long userId, Long purchaseId, UpdateRequest request);

    void deleteIndividualPurchase(Long userId, Long purchaseId);
}
