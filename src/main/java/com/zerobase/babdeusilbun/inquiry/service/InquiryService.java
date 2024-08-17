package com.zerobase.babdeusilbun.inquiry.service;

import com.zerobase.babdeusilbun.domain.Inquiry;
import com.zerobase.babdeusilbun.domain.InquiryImage;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.ListResponse;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.Request;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryImageDto;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface InquiryService {

  Page<Inquiry> getInquiryList(String statusFilter, Pageable pageable);

  Inquiry getInquiryInfo(Long inquiryId);

  void createInquiry(CustomUserDetails userDetails, Request request, List<MultipartFile> images);

  Page<InquiryImage> getInquiryImageList(Long inquiryId, Pageable pageable);

  void updateImageSequence(CustomUserDetails userDetails, Long inquiryId, Long imageId, Integer updatedSequence);

  void deleteImage(CustomUserDetails userDetails, Long inquiryId, Long imageId);
}
