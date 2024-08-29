package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.enums.EvaluateBadge;
import lombok.*;

import java.util.List;

import static com.zerobase.babdeusilbun.enums.EvaluateBadge.*;

public class EvaluateDto {

    @Data
    @Builder
    public static class MyEvaluates {
        List<PositiveEvaluate> positiveEvaluate;
        List<NegativeEvaluate> negativeEvaluate;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class EvaluateParticipantRequest {
        List<EvaluateBadge> positiveEvaluate;
        List<EvaluateBadge> negativeEvaluate;
    }

    public interface PositiveEvaluate {
        EvaluateBadge getContent();
        Long getCount();
    }
    public interface NegativeEvaluate {
        EvaluateBadge getContent();
        Long getCount();
    }

    public static void insertZeroValueInPositiveEvaluateArray(List<EvaluateDto.PositiveEvaluate> positiveEvaluateList) {
        boolean isInGOOD_COMMUNICATION = false;
        boolean isInGOOD_TIMECHECK = false;
        boolean isInGOOD_TOGETHER = false;
        boolean isInGOOD_RESPONSE = false;

        for(int i = 0; i < positiveEvaluateList.size(); i++) {
            switch(positiveEvaluateList.get(i).getContent()) {
                case GOOD_COMMUNICATION :
                    isInGOOD_COMMUNICATION = true;
                    break;
                case GOOD_TIMECHECK :
                    isInGOOD_TIMECHECK = true;
                    break;
                case GOOD_TOGETHER :
                    isInGOOD_TOGETHER = true;
                    break;
                case GOOD_RESPONSE :
                    isInGOOD_RESPONSE = true;
                    break;
            }
        }

        if(!isInGOOD_COMMUNICATION) {
            positiveEvaluateList.add(new EvaluateDto.PositiveEvaluate() {
                @Override
                public EvaluateBadge getContent() {
                    return GOOD_COMMUNICATION;
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }
        if(!isInGOOD_TIMECHECK) {
            positiveEvaluateList.add(new EvaluateDto.PositiveEvaluate() {
                @Override
                public EvaluateBadge getContent() {
                    return GOOD_TIMECHECK;
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }
        if(!isInGOOD_TOGETHER) {
            positiveEvaluateList.add(new EvaluateDto.PositiveEvaluate() {
                @Override
                public EvaluateBadge getContent() {
                    return GOOD_TOGETHER;
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }
        if(!isInGOOD_RESPONSE) {
            positiveEvaluateList.add(new EvaluateDto.PositiveEvaluate() {
                @Override
                public EvaluateBadge getContent() {
                    return GOOD_RESPONSE;
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }
    }

    public static void insertZeroValueInNegativeEvaluateArray(List<EvaluateDto.NegativeEvaluate> negativeEvaluateList) {
        boolean isInBAD_RESPONSE = false;
        boolean isInBAD_TIMECHECK = false;
        boolean isInBAD_TOGETHER = false;

        for(int i = 0; i < negativeEvaluateList.size(); i++) {
            switch(negativeEvaluateList.get(i).getContent()) {
                case BAD_RESPONSE :
                    isInBAD_RESPONSE = true;
                    break;
                case BAD_TIMECHECK :
                    isInBAD_TIMECHECK = true;
                    break;
                case BAD_TOGETHER :
                    isInBAD_TOGETHER = true;
                    break;
            }
        }

        if(!isInBAD_RESPONSE) {
            negativeEvaluateList.add(new EvaluateDto.NegativeEvaluate() {
                @Override
                public EvaluateBadge getContent() {
                    return BAD_RESPONSE;
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }
        if(!isInBAD_TIMECHECK) {
            negativeEvaluateList.add(new EvaluateDto.NegativeEvaluate() {
                @Override
                public EvaluateBadge getContent() {
                    return BAD_TIMECHECK;
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }
        if(!isInBAD_TOGETHER) {
            negativeEvaluateList.add(new EvaluateDto.NegativeEvaluate() {
                @Override
                public EvaluateBadge getContent() {
                    return BAD_TOGETHER;
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }
    }
}
