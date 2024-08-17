package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.enums.EvaluateBadge;
import lombok.*;

import java.util.List;

public class EvaluateDto {

    @Data
    @Builder
    public static class MyEvaluates {
        List<PositiveEvaluate> positiveEvaluate;
        List<NegativeEvaluate> negativeEvaluate;
    }

    public interface PositiveEvaluate {
        EvaluateBadge getContent();
        Long getCount();
    }
    public interface NegativeEvaluate {
        EvaluateBadge getContent();
        Long getCount();
    }
}
