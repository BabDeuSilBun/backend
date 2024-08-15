package com.zerobase.babdeusilbun.inquiry.service.impl;

import static com.zerobase.babdeusilbun.exception.ErrorCode.*;

import com.zerobase.babdeusilbun.domain.Inquiry;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.DetailResponse;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.ListResponse;
import com.zerobase.babdeusilbun.inquiry.service.InquiryService;
import com.zerobase.babdeusilbun.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

  private final InquiryRepository inquiryRepository;


  @Override
  public Page<ListResponse> getInquiryList(String statusFilter, Pageable pageable) {
    return inquiryRepository
        .findInquiryList(statusFilter, pageable).map(ListResponse::fromEntity);
  }

  @Override
  public DetailResponse getInquiryInfo(Long inquiryId) {
    return DetailResponse.fromEntity(findInquiryById(inquiryId));
  }

  private Inquiry findInquiryById(Long inquiryId) {
    return inquiryRepository.findById(inquiryId)
        .orElseThrow(() -> new CustomException(INQUIRY_NOT_FOUND));
  }
}
