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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
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
  @Transactional
  public void createInquiry
      (CustomUserDetails userDetails, Request request, List<MultipartFile> images) {

    User findUser = findUserByUserDetails(userDetails);

    // 새로운 Inquiry 생성
    Inquiry createdInquiry = createNewInquiry(findUser, request);
    Inquiry savedInquiry = inquiryRepository.save(createdInquiry);

    // Inquiry image 저장
    List<String> uploadedImageUrlList =
        imageComponent.uploadImageList(images, INQUIRY_IMAGE_FOLDER);

    List<InquiryImage> inquiryImageList = mapUrlToImageEntity(savedInquiry, uploadedImageUrlList);
    inquiryImageRepository.saveAll(inquiryImageList);

  }

  @Override
  public Page<InquiryImageDto> getInquiryImageList(Long inquiryId, Pageable pageable) {

    return inquiryImageRepository.findAllByInquiryOrderBySequence(findInquiryById(inquiryId), pageable)
        .map(InquiryImageDto::fromEntity);
  }

  @Override
  @Transactional
  public void updateImageSequence
      (CustomUserDetails userDetails, Long inquiryId, Long imageId, Integer updatedSequence) {

    User findUser = findUserByUserDetails(userDetails);
    Inquiry findInquiry = findInquiryById(inquiryId);

    // 해당 문의글을 작성한 사용자인지 검증
    verifyInquiryWriter(findUser, findInquiry);

    InquiryImage findInquiryImage = findInquiryImageById(imageId);
    int originalSequence = findInquiryImage.getSequence();

    List<InquiryImage> inquiryImageList = inquiryImageRepository.findAllByInquiry(findInquiry);

    // 변경할 이미지 순서가 올바른 범위에 있는지 검증
    verifyImageSequenceRequest(inquiryImageList, updatedSequence);

    // sequence 재배치
    InquiryImage targetInquiryImage = inquiryImageList.remove(originalSequence - 1);
    inquiryImageList.add(updatedSequence - 1, targetInquiryImage);

    allocateSequence(inquiryImageList);
  }

  private void verifyImageSequenceRequest(List<InquiryImage> inquiryImageList, Integer updatedSequence) {
    if (updatedSequence < 1 || updatedSequence > inquiryImageList.size() + 1) {
      throw new CustomException(INQUIRY_IMAGE_SEQUENCE_INVALID);
    }
  }

  private void verifyInquiryWriter(User findUser, Inquiry findInquiry) {
    if (findUser != findInquiry.getUser()) {
      throw new CustomException(INQUIRY_WRITER_NOT_MATCH);
    }
  }

  // image url을 image 객체로 변환
  private List<InquiryImage> mapUrlToImageEntity(Inquiry inquiry, List<String> uploadedImageList) {

    return allocateSequence(
        createInquiryImageEntity(inquiry, uploadedImageList)
    );
  }

  // InquiryImage 엔티티 생성
  private List<InquiryImage> createInquiryImageEntity(Inquiry inquiry, List<String> uploadedImageList) {
    return uploadedImageList.stream().map(url ->
        InquiryImage.builder().inquiry(inquiry).url(url).build()
    ).toList();
  }

  // sequence 할당
  private List<InquiryImage> allocateSequence(List<InquiryImage> inquiryImageList) {
    int sequence = 1;

    for (InquiryImage inquiryImage : inquiryImageList) {
      inquiryImage.changeSequence(sequence++);
    }
    return inquiryImageList;
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

  private InquiryImage findInquiryImageById(Long imageId) {
    return inquiryImageRepository.findById(imageId)
        .orElseThrow(() -> new CustomException(INQUIRY_IMAGE_NOT_FOUND));
  }
}
