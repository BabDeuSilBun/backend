package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.domain.Evaluate;
import com.zerobase.babdeusilbun.dto.EvaluateDto;
import com.zerobase.babdeusilbun.dto.EvaluateDto.EvaluateParticipantRequest;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import java.util.List;

public interface EvaluateService {
    EvaluateDto.MyEvaluates getEvaluates(Long userId);

    List<Evaluate> evaluateParticipant(Long userId, Long meetingId, Long participantId, EvaluateParticipantRequest request);
}
