package com.zerobase.babdeusilbun.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.babdeusilbun.domain.Major;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class MajorDto {
    @Data
    @Builder
    public static class Principal {
        private Long id;
        private String name;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static MajorDto.Principal fromEntity(Major major) {
            return MajorDto.Principal.builder()
                    .id(major.getId())
                    .name(major.getName())
                    .createdAt(major.getCreatedAt())
                    .updatedAt(major.getUpdatedAt())
                    .build();
        }
    }

    @Data
    @Builder
    public static class Information {
        private Long id;
        private String name;

        public static SchoolDto.Information fromEntity(Major major) {
            return SchoolDto.Information.builder()
                    .id(major.getId())
                    .name(major.getName())
                    .build();
        }

        @QueryProjection
        public Information(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
