package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Category;
import com.zerobase.babdeusilbun.repository.custom.CustomCategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CustomCategoryRepository {
}
