package com.zerobase.babdeusilbun.inquiry.service.impl;

import static com.zerobase.babdeusilbun.enums.InquiryStatus.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;
import static com.zerobase.babdeusilbun.util.ImageUtility.*;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Inquiry;
import com.zerobase.babdeusilbun.domain.InquiryImage;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.DetailResponse;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.ListResponse;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.Request;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryImageDto;
import com.zerobase.babdeusilbun.inquiry.service.InquiryService;
import com.zerobase.babdeusilbun.repository.InquiryImageRepository;
import com.zerobase.babdeusilbun.repository.InquiryRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

  private final InquiryRepository inquiryRepository;
  private final InquiryImageRepository inquiryImageRepository;
  private final UserRepository userRepository;

  private final ImageComponent imageComponent;


  @Override
  public Page<ListResponse> getInquiryList(String statusFilter, Pageable pageable) {
    return inquiryRepository
        .findInquiryList(statusFilter, pageable).map(ListResponse::fromEntity);
  }

  @Override
  public DetailResponse getInquiryInfo(Long inquiryId) {
    return DetailResponse.fromEntity(findInquiryById(inquiryId));
  }

  @Override
  public void createInquiry
      (CustomUserDetails userDetails, Request request, List<MultipartFile> images) {

    User findUser = findUserByUserDetails(userDetails);

    // 새로운 Inquiry 생성
    Inquiry createdInquiry = createNewInquiry(findUser, request);
    Inquiry savedInquiry = inquiryRepository.save(createdInquiry);

    // Inquiry image 저장
    List<String> uploadedImageUrlList =
        imageComponent.uploadImageList(images, INQUIRY_IMAGE_FOLDER);

    List<InquiryImage> inquiryImageList = mapToInquiryImageEntity(savedInquiry, uploadedImageUrlList);
    inquiryImageRepository.saveAll(inquiryImageList);

  }

  @Override
  public Page<InquiryImageDto> getInquiryImageList(Long inquiryId, Pageable pageable) {

    return inquiryImageRepository.findAllByInquiryOrderBySequence(findInquiryById(inquiryId), pageable)
        .map(InquiryImageDto::fromEntity);
  }

  private List<InquiryImage> mapToInquiryImageEntity(Inquiry inquiry, List<String> uploadedImageList) {
    return fromUrlToImageEntity(inquiry, uploadedImageList);
  }

  private List<InquiryImage> fromUrlToImageEntity(Inquiry inquiry, List<String> uploadedImageList) {
    AtomicInteger sequence = new AtomicInteger(1);

    return uploadedImageList.stream().map(url -> InquiryImage.builder()
        .inquiry(inquiry)
        .url(url)
        .sequence(sequence.getAndIncrement())
        .build()).toList();
  }

  private Inquiry createNewInquiry(User user, Request request) {
    return Inquiry.builder()
        .user(user)
        .title(request.getTitle())
        .content(request.getContent())
        .status(PENDING)
        .build();
  }

  private User findUserByUserDetails(CustomUserDetails userDetails) {
    return userRepository.findById(userDetails.getId())
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
  }

  private Inquiry findInquiryById(Long inquiryId) {
    return inquiryRepository.findById(inquiryId)
        .orElseThrow(() -> new CustomException(INQUIRY_NOT_FOUND));
  }
}
