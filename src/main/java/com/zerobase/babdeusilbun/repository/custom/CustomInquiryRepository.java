package com.zerobase.babdeusilbun.repository.custom;

import com.zerobase.babdeusilbun.domain.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomInquiryRepository {

  Page<Inquiry> findInquiryList(String statusFilter, Pageable pageable);

}
