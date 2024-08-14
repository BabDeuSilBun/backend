package com.zerobase.babdeusilbun.inquiry.service.impl;

import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.ListResponse;
import com.zerobase.babdeusilbun.inquiry.service.InquiryService;
import com.zerobase.babdeusilbun.repository.InquiryQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

  private final InquiryQueryRepository inquiryQueryRepository;


  @Override
  public Page<ListResponse> getInquiryList(String statusFilter, Pageable pageable) {
    return inquiryQueryRepository
        .findInquiryList(statusFilter, pageable).map(ListResponse::fromEntity);
  }
}
