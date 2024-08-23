package com.zerobase.babdeusilbun.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

public class TeamPurchaseDto {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class CreateRequest {
        @NotNull(message = "menuId는 정수 값이 들어와야 합니다.")
        private Long menuId;

        @Positive(message = "quantity는 1이상의 정수 값이 들어와야 합니다.")
        private Integer quantity;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class UpdateRequest {
        @Positive(message = "quantity는 1이상의 정수 값이 들어와야 합니다.")
        private Integer quantity;
    }
}
