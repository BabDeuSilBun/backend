package com.zerobase.babdeusilbun.repository;

import com.zerobase.babdeusilbun.domain.Inquiry;
import com.zerobase.babdeusilbun.domain.InquiryImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryImageRepository extends JpaRepository<InquiryImage, Long> {

  Page<InquiryImage> findAllByInquiryOrderBySequence(Inquiry inquiry, Pageable pageable);
}
