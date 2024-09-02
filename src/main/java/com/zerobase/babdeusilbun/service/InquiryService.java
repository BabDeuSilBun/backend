package com.zerobase.babdeusilbun.service;

import com.zerobase.babdeusilbun.domain.Inquiry;
import com.zerobase.babdeusilbun.domain.InquiryImage;
import com.zerobase.babdeusilbun.dto.InquiryDto.Request;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface InquiryService {

  Page<Inquiry> getInquiryList(Long userId, Pageable pageable);

  void createInquiry(Long userId, Request request, List<MultipartFile> images);

  List<InquiryImage> getInquiryImageList(Long userId, Long inquiryId);

  void updateImageSequence(Long userId, Long inquiryId, Long imageId, Integer updatedSequence);

  void deleteImage(Long userId, Long inquiryId, Long imageId);
}
