package com.zerobase.babdeusilbun.inquiry.service;

import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.ListResponse;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.Request;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface InquiryService {

  Page<ListResponse> getInquiryList(String statusFilter, Pageable pageable);

  InquiryDto.DetailResponse getInquiryInfo(Long inquiryId);

  void createInquiry(CustomUserDetails userDetails, Request request, List<MultipartFile> images);
}
