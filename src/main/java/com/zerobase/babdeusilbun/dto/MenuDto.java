package com.zerobase.babdeusilbun.dto;

import com.zerobase.babdeusilbun.domain.Menu;
import com.zerobase.babdeusilbun.domain.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

public class MenuDto {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class CreateRequest {
        @NotBlank(message = "name 항목은 빈값이 올 수 없습니다.")
        private String name;

        @NotBlank(message = "description 항목은 빈값이 올 수 없습니다.")
        private String description;

        private String image;

        @PositiveOrZero(message = "price 항목은 0이상만 올 수 있습니다.")
        private long price;

        public Menu toEntity(Store store) {
            return Menu.builder()
                    .store(store)
                    .name(name)
                    .image(image)
                    .description(description)
                    .price(price)
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class UpdateRequest {
        private String name;
        private String description;
        private String image;
        private long price;

        public Menu toEntity(Store store) {
            return Menu.builder()
                    .store(store)
                    .name(name)
                    .image(image)
                    .description(description)
                    .price(price)
                    .build();
        }
    }
}
