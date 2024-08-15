package com.zerobase.babdeusilbun.inquiry.service;

import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.ListResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InquiryService {

  Page<ListResponse> getInquiryList(String statusFilter, Pageable pageable);

  InquiryDto.DetailResponse getInquiryInfo(Long inquiryId);

}
