package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.domain.TeamPurchase;
import com.zerobase.babdeusilbun.dto.TeamPurchaseDto.CreateRequest;
import com.zerobase.babdeusilbun.dto.TeamPurchaseDto.UpdateRequest;

public interface TeamPurchaseService {
    TeamPurchase createTeamPurchase(Long userId, Long meetingId, CreateRequest request);

    TeamPurchase updateTeamPurchase(Long userId, Long purchaseId, UpdateRequest request);

    void deleteTeamPurchase(Long userId, Long purchaseId);
}
