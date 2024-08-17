package com.zerobase.babdeusilbun.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.babdeusilbun.domain.School;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class SchoolDto {
  @Data
  @Builder
  public static class Principal {
    private Long id;
    private String name;
    private String campus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Principal fromEntity(School school) {
      return Principal.builder()
          .id(school.getId())
          .name(school.getName())
          .campus(school.getCampus())
          .createdAt(school.getCreatedAt())
          .updatedAt(school.getUpdatedAt())
          .build();
    }
  }

  @Data
  @Builder
  public static class Information {
    private Long id;
    private String name;
    private String campus;

    public static Information fromEntity(School school) {
      return Information.builder()
          .id(school.getId())
          .name(school.getName().replaceAll("(\\s|\\()[^\\s]+((캠퍼스)|\\))$", ""))
          .campus(school.getCampus())
          .build();
    }

    @QueryProjection
    public Information(Long id, String name, String campus) {
      this.id = id;
      this.name = name.replaceAll("(\\s|\\()[^\\s]+((캠퍼스)|\\))$", "");
      this.campus = campus;
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class IdsRequest {
    private Set<Long> schoolIds = new HashSet<>();
  }
}
