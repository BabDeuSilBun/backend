package com.zerobase.babdeusilbun.repository.custom;

import com.zerobase.babdeusilbun.dto.CategoryDto.Information;
import org.springframework.data.domain.Page;

public interface CustomCategoryRepository {
  Page<Information> getAllCategories(int page, int size);
}
