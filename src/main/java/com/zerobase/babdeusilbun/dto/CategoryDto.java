package com.zerobase.babdeusilbun.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.babdeusilbun.domain.Category;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class CategoryDto {
  @Data
  @Builder
  public static class Information {
    private Long categoryId;
    private String name;

    public static Information fromEntity(Category category) {
      return Information.builder()
          .categoryId(category.getId())
          .name(category.getName())
          .build();
    }

    @QueryProjection
    public Information(Long categoryId, String name) {
      this.categoryId = categoryId;
      this.name = name;
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class IdsRequest {
    private Set<Long> categoryIds = new HashSet<>();
  }
}
