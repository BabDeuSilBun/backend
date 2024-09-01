package com.zerobase.babdeusilbun.service.impl;

import static com.zerobase.babdeusilbun.enums.InquiryStatus.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;
import static com.zerobase.babdeusilbun.util.ImageUtility.*;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Inquiry;
import com.zerobase.babdeusilbun.domain.InquiryImage;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.dto.InquiryDto.Request;
import com.zerobase.babdeusilbun.service.InquiryService;
import com.zerobase.babdeusilbun.repository.InquiryImageRepository;
import com.zerobase.babdeusilbun.repository.InquiryRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import java.util.List;
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
  public Page<Inquiry> getInquiryList(Long userId, Pageable pageable) {

    return inquiryRepository.findAllByUserOrderByCreatedAtDesc(findUserById(userId), pageable);
  }

  @Override
  @Transactional
  public void createInquiry
      (Long userId, Request request, List<MultipartFile> images) {

    User findUser = findUserById(userId);

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
  public List<InquiryImage> getInquiryImageList(Long userId, Long inquiryId) {

    User findUser = findUserById(userId);
    Inquiry findInquiry = findInquiryById(inquiryId);

    verifyInquiryWriter(findUser, findInquiry);

    return inquiryImageRepository.findAllByInquiryOrderBySequence(findInquiry);
  }

  @Override
  @Transactional
  public void updateImageSequence
      (Long userId, Long inquiryId, Long imageId, Integer updatedSequence) {

    User findUser = findUserById(userId);
    Inquiry findInquiry = findInquiryById(inquiryId);

    // 해당 문의글을 작성한 사용자인지 검증
    verifyInquiryWriter(findUser, findInquiry);

    // 문의글 답변이 등록된 상태인지 확인
    verifyInquiryIsComplete(findInquiry);

    InquiryImage findInquiryImage = findInquiryImageById(imageId);
    int originalSequence = findInquiryImage.getSequence();

    List<InquiryImage> inquiryImageList =
        inquiryImageRepository.findAllByInquiryOrderBySequence(findInquiry);

    // 변경할 이미지 순서가 올바른 범위에 있는지 검증
    verifyImageSequenceRequest(inquiryImageList, updatedSequence);

    // sequence 재배치
    InquiryImage targetInquiryImage = inquiryImageList.remove(originalSequence - 1);
    inquiryImageList.add(updatedSequence - 1, targetInquiryImage);

    allocateSequence(inquiryImageList);
  }

  @Override
  @Transactional
  public void deleteImage(Long userId, Long inquiryId, Long imageId) {

    User findUser = findUserById(userId);
    Inquiry findInquiry = findInquiryById(inquiryId);

    // 해당 문의글 작성자인지 확인
    verifyInquiryWriter(findUser, findInquiry);

    // 문의글 답변이 등록된 상태인지 확인
    verifyInquiryIsComplete(findInquiry);

    InquiryImage findImage = findInquiryImageById(imageId);
    Integer deletedImageSequence = findImage.getSequence();

    // 이미지가 해당 게시글의 이미지 인지 확인
    verifyImagePossession(findImage, findInquiry);

    // 나머지 이미지 sequence 재할당
    List<InquiryImage> imageList = inquiryImageRepository.findAllByInquiry(findInquiry);
    imageList.remove(deletedImageSequence - 1);
    allocateSequence(imageList);

    inquiryImageRepository.delete(findImage);
  }

  private void verifyInquiryIsComplete(Inquiry findInquiry) {
    if (findInquiry.getStatus() == COMPLETED) {
      throw new CustomException(INQUIRY_ALREADY_COMPLETE);
    }
  }

  private void verifyImagePossession(InquiryImage findImage, Inquiry findInquiry) {
    if (findImage.getInquiry() != findInquiry) {
      throw new CustomException(INQUIRY_WRITER_NOT_MATCH);
    }
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
  // 1. image entity 생성
  // 2. sequence 할당
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

  private User findUserById(Long userId) {
    return userRepository.findById(userId)
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
