package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Inquiry;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.repository.custom.CustomInquiryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, CustomInquiryRepository {

  Page<Inquiry> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
