package com.zerobase.backend.init;

import com.zerobase.backend.domain.Major;
import com.zerobase.backend.domain.School;
import com.zerobase.backend.repository.MajorRepository;
import com.zerobase.backend.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 테스트용 더미 데이터
 */
@Profile("test")
@Configuration
@RequiredArgsConstructor
public class InitData {

  private final SchoolRepository schoolRepository;
  private final MajorRepository majorRepository;

  @Bean
  public CommandLineRunner loadData() {

    School schoolA = School.builder().name("schoolA").campus("campusA").build();
    School schoolB = School.builder().name("schoolB").campus("campusB").build();
    Major majorA = Major.builder().name("majorA").build();
    Major majorB = Major.builder().name("majorB").build();

    return args -> {
      schoolRepository.save(schoolA);
      schoolRepository.save(schoolB);
      majorRepository.save(majorA);
      majorRepository.save(majorB);
    };
  }



}
