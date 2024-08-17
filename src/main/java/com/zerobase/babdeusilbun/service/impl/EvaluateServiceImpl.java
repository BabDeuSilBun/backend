package com.zerobase.babdeusilbun.service.impl;

import com.zerobase.babdeusilbun.dto.EvaluateDto;
import com.zerobase.babdeusilbun.repository.EvaluateRepository;
import com.zerobase.babdeusilbun.service.EvaluateService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zerobase.babdeusilbun.security.constants.SecurityConstantsUtil.getOriginalEmail;

@Service
@AllArgsConstructor
public class EvaluateServiceImpl implements EvaluateService {

    private final EvaluateRepository evaluateRepository;

    // 현재 로그인한 사람의 이메일 정보를 가져오는 함수
    private String getLoginUserEmail() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContextHolderStrategy()
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return getOriginalEmail(userDetails.getUsername());
    }

    @Override
    public EvaluateDto.MyEvaluates getEvaluates() {
        String loginUserEmail = getLoginUserEmail();
        List<EvaluateDto.PositiveEvaluate> positiveEvaluateList = evaluateRepository.findPositiveEvaluatesByEmail(loginUserEmail);
        List<EvaluateDto.NegativeEvaluate> negativeEvaluateList = evaluateRepository.findNegativeEvaluatesByEmail(loginUserEmail);

        EvaluateDto.MyEvaluates evaluates = EvaluateDto.MyEvaluates.builder().positiveEvaluate(positiveEvaluateList).negativeEvaluate(negativeEvaluateList).build();
        return evaluates;
    }
}
