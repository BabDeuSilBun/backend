package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.enums.EvaluateBadge;
import lombok.*;

import java.util.ArrayList;
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
        String getContent();
        Long getCount();
    }
    public interface NegativeEvaluate {
        String getContent();
        Long getCount();
    }

    public static List<EvaluateDto.PositiveEvaluate> insertZeroValueInPositiveEvaluateArray(List<EvaluateDto.PositiveEvaluate> positiveEvaluateList) {
        List<EvaluateDto.PositiveEvaluate> newPositiveEvaluateList = new ArrayList<>();

        boolean isInGOOD_COMMUNICATION = false;
        boolean isInGOOD_TIMECHECK = false;
        boolean isInGOOD_TOGETHER = false;
        boolean isInGOOD_RESPONSE = false;

        for(int i = 0; i < positiveEvaluateList.size(); i++) {
            String content = positiveEvaluateList.get(i).getContent();
            Long cnt = positiveEvaluateList.get(i).getCount();

            if (content.equals(GOOD_COMMUNICATION.name())) {
                isInGOOD_COMMUNICATION = true;
                newPositiveEvaluateList.add(new PositiveEvaluate() {
                    @Override
                    public String getContent() {
                        return GOOD_COMMUNICATION.getDescription();
                    }

                    @Override
                    public Long getCount() {
                        return cnt;
                    }
                });
            } else if (content.equals(GOOD_TIMECHECK.name())) {
                isInGOOD_TIMECHECK = true;
                newPositiveEvaluateList.add(new PositiveEvaluate() {
                    @Override
                    public String getContent() {
                        return GOOD_TIMECHECK.getDescription();
                    }

                    @Override
                    public Long getCount() {
                        return cnt;
                    }
                });
            } else if (content.equals(GOOD_TOGETHER)) {
                isInGOOD_TOGETHER = true;
                newPositiveEvaluateList.add(new PositiveEvaluate() {
                    @Override
                    public String getContent() {
                        return GOOD_TOGETHER.getDescription();
                    }

                    @Override
                    public Long getCount() {
                        return cnt;
                    }
                });
            } else if (content.equals(GOOD_RESPONSE)) {
                isInGOOD_RESPONSE = true;
                newPositiveEvaluateList.add(new PositiveEvaluate() {
                    @Override
                    public String getContent() {
                        return GOOD_RESPONSE.getDescription();
                    }

                    @Override
                    public Long getCount() {
                        return cnt;
                    }
                });
            }
        }

        if(!isInGOOD_COMMUNICATION) {
            newPositiveEvaluateList.add(new EvaluateDto.PositiveEvaluate() {
                @Override
                public String getContent() {
                    return GOOD_COMMUNICATION.getDescription();
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }
        if(!isInGOOD_TIMECHECK) {
            newPositiveEvaluateList.add(new EvaluateDto.PositiveEvaluate() {
                @Override
                public String getContent() {
                    return GOOD_TIMECHECK.getDescription();
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }
        if(!isInGOOD_TOGETHER) {
            newPositiveEvaluateList.add(new EvaluateDto.PositiveEvaluate() {
                @Override
                public String getContent() {
                    return GOOD_TOGETHER.getDescription();
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }
        if(!isInGOOD_RESPONSE) {
            newPositiveEvaluateList.add(new EvaluateDto.PositiveEvaluate() {
                @Override
                public String getContent() {
                    return GOOD_RESPONSE.getDescription();
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }

        return newPositiveEvaluateList;
    }

    public static List<EvaluateDto.NegativeEvaluate> insertZeroValueInNegativeEvaluateArray(List<EvaluateDto.NegativeEvaluate> negativeEvaluateList) {
        List<EvaluateDto.NegativeEvaluate> newNegativeEvaluateList = new ArrayList<>();

        boolean isInBAD_RESPONSE = false;
        boolean isInBAD_TIMECHECK = false;
        boolean isInBAD_TOGETHER = false;

        for(int i = 0; i < negativeEvaluateList.size(); i++) {
            String content = negativeEvaluateList.get(i).getContent();
            Long cnt = negativeEvaluateList.get(i).getCount();

            if (content.equals(BAD_RESPONSE.name())) {
                isInBAD_RESPONSE = true;
                newNegativeEvaluateList.add(new NegativeEvaluate() {
                    @Override
                    public String getContent() {
                        return BAD_RESPONSE.getDescription();
                    }

                    @Override
                    public Long getCount() {
                        return cnt;
                    }
                });
            } else if (content.equals(BAD_TIMECHECK.name())) {
                isInBAD_TIMECHECK = true;
                newNegativeEvaluateList.add(new NegativeEvaluate() {
                    @Override
                    public String getContent() {
                        return BAD_TIMECHECK.getDescription();
                    }

                    @Override
                    public Long getCount() {
                        return cnt;
                    }
                });
            } else if (content.equals(BAD_TOGETHER.name())) {
                isInBAD_TOGETHER = true;
                newNegativeEvaluateList.add(new NegativeEvaluate() {
                    @Override
                    public String getContent() {
                        return BAD_TOGETHER.getDescription();
                    }

                    @Override
                    public Long getCount() {
                        return cnt;
                    }
                });
            }
        }

        if(!isInBAD_RESPONSE) {
            newNegativeEvaluateList.add(new EvaluateDto.NegativeEvaluate() {
                @Override
                public String getContent() {
                    return BAD_RESPONSE.getDescription();
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }
        if(!isInBAD_TIMECHECK) {
            newNegativeEvaluateList.add(new EvaluateDto.NegativeEvaluate() {
                @Override
                public String getContent() {
                    return BAD_TIMECHECK.getDescription();
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }
        if(!isInBAD_TOGETHER) {
            newNegativeEvaluateList.add(new EvaluateDto.NegativeEvaluate() {
                @Override
                public String getContent() {
                    return BAD_TOGETHER.getDescription();
                }
                @Override
                public Long getCount() {
                    return 0L;
                }
            });
        }

        return newNegativeEvaluateList;
    }

}
