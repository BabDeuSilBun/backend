package com.zerobase.babdeusilbun.service.impl;

import com.zerobase.babdeusilbun.dto.EvaluateDto;
import com.zerobase.babdeusilbun.repository.EvaluateRepository;
import com.zerobase.babdeusilbun.service.EvaluateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EvaluateServiceImpl implements EvaluateService {

    private final EvaluateRepository evaluateRepository;

    @Override
    public EvaluateDto.MyEvaluates getEvaluates(Long userId) {
        List<EvaluateDto.PositiveEvaluate> positiveEvaluateList = evaluateRepository.findPositiveEvaluatesByUserId(userId);
        List<EvaluateDto.NegativeEvaluate> negativeEvaluateList = evaluateRepository.findNegativeEvaluatesByUserId(userId);

        EvaluateDto.MyEvaluates evaluates = EvaluateDto.MyEvaluates.builder().positiveEvaluate(positiveEvaluateList).negativeEvaluate(negativeEvaluateList).build();
        return evaluates;
    }
}
