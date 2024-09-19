package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Major;
import com.zerobase.babdeusilbun.dto.SchoolDto;
import com.zerobase.babdeusilbun.repository.custom.CustomMajorRepository;
import com.zerobase.babdeusilbun.repository.custom.CustomSchoolRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<Major, Long>, CustomMajorRepository {
}
