package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.dto.EntrepreneurDto.MyPage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EntrepreneurRepository extends JpaRepository<Entrepreneur, Long> {

  boolean existsByEmail(String email);

  Optional<Entrepreneur> findByEmail(String email);

  Optional<Entrepreneur> findByIdAndDeletedAtIsNull(Long entrepreneurId);

  @Query(value = "select e.id as entrepreneurId, e.email as email, e.name as name, " +
          "e.phoneNumber as phoneNumber, e.businessNumber as businessNumber, e.address as address, e.image as image " +
          "from Entrepreneur as e " +
          "where e.id = :entrepreneurId and e.deletedAt is null")
  Optional<MyPage> findMyPageByUserId(Long entrepreneurId);
}
