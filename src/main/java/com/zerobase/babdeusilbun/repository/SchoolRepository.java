package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.School;
import com.zerobase.babdeusilbun.repository.custom.CustomSchoolRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long>, CustomSchoolRepository {
}
