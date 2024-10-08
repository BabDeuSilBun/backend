package com.zerobase.babdeusilbun.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.component.ImageComponent;
import com.zerobase.babdeusilbun.domain.Inquiry;
import com.zerobase.babdeusilbun.domain.InquiryImage;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.enums.InquiryStatus;
import com.zerobase.babdeusilbun.dto.InquiryDto.Request;
import com.zerobase.babdeusilbun.service.impl.InquiryServiceImpl;
import com.zerobase.babdeusilbun.repository.InquiryImageRepository;
import com.zerobase.babdeusilbun.repository.InquiryRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.security.dto.CustomUserDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class InquiryServiceTest {

  @InjectMocks
  private InquiryServiceImpl inquiryService;
  @Mock
  private InquiryRepository inquiryRepository;
  @Mock
  private InquiryImageRepository inquiryImageRepository;
  @Mock
  private ImageComponent imageComponent;
  @Mock
  private UserRepository userRepository;

  @Test
  @DisplayName("문의 목록 조회")
  void InquiryList() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Inquiry pending = Inquiry.builder()
        .user(user).title("title1").content("content1")
        .status(InquiryStatus.PENDING).build();
    Inquiry complete = Inquiry.builder()
        .user(user).title("title1").content("content1")
        .status(InquiryStatus.COMPLETED).build();

    Pageable pageable = PageRequest.of(0, 2);

    Page<Inquiry> list = new PageImpl<>(List.of(pending));

    when(inquiryRepository.findAllByUserOrderByCreatedAtDesc(user, pageable)).thenReturn(list);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    // when
    Page<Inquiry> inquiryList = inquiryService.getInquiryList(1L, pageable);
    List<Inquiry> content = inquiryList.getContent();

    // then
    assertThat(inquiryList.getTotalPages()).isEqualTo(1);
    assertThat(inquiryList.getSize()).isEqualTo(1);
    assertThat(inquiryList.getTotalElements()).isEqualTo(1);
    assertThat(content.size()).isEqualTo(1);
    assertThat(content.getFirst().getTitle()).isEqualTo("title1");
    assertThat(content.getFirst().getStatus()).isEqualTo(InquiryStatus.PENDING);
  }

  @Test
  @DisplayName("문의 게시글 작성")
  void createInquiry() throws Exception {
    // given
    User user = User.builder().id(1L).email("test").build();
    CustomUserDetails customUserDetails = new CustomUserDetails(user);

    Request request = Request.builder()
        .title("title request")
        .content("content request")
        .build();

    List<String> imageUrlList = List.of("url1", "url2");
    when(imageComponent.uploadImageList(any(), anyString())).thenReturn(imageUrlList);
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    // when
    inquiryService.createInquiry(customUserDetails.getId(), request, new ArrayList<>());

    // then
    verify(inquiryImageRepository, times(1)).saveAll(any());
    verify(inquiryRepository, times(1)).save(any());
    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("문의 이미지 조회")
  void getInquiryImageList() throws Exception {
    // given
    User user = User.builder().id(1L).email("test").build();
    CustomUserDetails customUserDetails = new CustomUserDetails(user);

    Inquiry inquiry = Inquiry.builder().id(1L).user(user).build();
    InquiryImage image1 = InquiryImage.builder().inquiry(inquiry).url("url1").sequence(1).build();
    InquiryImage image2 = InquiryImage.builder().inquiry(inquiry).url("url1").sequence(2).build();
    InquiryImage image3 = InquiryImage.builder().inquiry(inquiry).url("url1").sequence(3).build();

    List list = List.of(image1, image2, image3);

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    when(inquiryRepository.findById(anyLong())).thenReturn(Optional.of(inquiry));
    when(inquiryImageRepository.findAllByInquiryOrderBySequence(inquiry)).thenReturn(list);

    // when
    List<InquiryImage> result = inquiryService.getInquiryImageList(customUserDetails.getId(), 1L);

    // then
    verify(inquiryRepository, times(1)).findById(1L);
    verify(inquiryImageRepository, times(1)).findAllByInquiryOrderBySequence(inquiry);
    assertThat(result.size()).isEqualTo(3);
    assertThat(result.getFirst().getUrl()).isEqualTo(image1.getUrl());
    assertThat(result.get(2).getUrl()).isEqualTo(image2.getUrl());
    assertThat(result.getLast().getUrl()).isEqualTo(image3.getUrl());
  }

  @Test
  @DisplayName("문의 이미지 순서 변경")
  void updateImageSequence() throws Exception {
    // given
    User user = User.builder().id(1L).email("test").build();
    CustomUserDetails customUserDetails = new CustomUserDetails(user);

    Inquiry inquiry = Inquiry.builder().id(1L).user(user).build();

    InquiryImage image1 = InquiryImage.builder().id(1L).inquiry(inquiry).url("1").sequence(1).build();
    InquiryImage image2 = InquiryImage.builder().id(2L).inquiry(inquiry).url("2").sequence(2).build();
    InquiryImage image3 = InquiryImage.builder().id(3L).inquiry(inquiry).url("3").sequence(3).build();
    InquiryImage image4 = InquiryImage.builder().id(4L).inquiry(inquiry).url("4").sequence(4).build();
    InquiryImage image5 = InquiryImage.builder().id(5L).inquiry(inquiry).url("5").sequence(5).build();
    List<InquiryImage> imageList = new ArrayList<>();
    imageList.add(image1);
    imageList.add(image2);
    imageList.add(image3);
    imageList.add(image4);
    imageList.add(image5);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(inquiryRepository.findById(1L)).thenReturn(Optional.of(inquiry));
//    when(inquiryImageRepository.findById(1L)).thenReturn(Optional.of(image1));
    when(inquiryImageRepository.findById(2L)).thenReturn(Optional.of(image2));
//    when(inquiryImageRepository.findById(3L)).thenReturn(Optional.of(image3));
//    when(inquiryImageRepository.findById(4L)).thenReturn(Optional.of(image4));
//    when(inquiryImageRepository.findById(5L)).thenReturn(Optional.of(image5));
    when(inquiryImageRepository.findAllByInquiry(any())).thenReturn(imageList);

    // when
    inquiryService.updateImageSequence(customUserDetails.getId(), 1L, 2L, 4);

    // then
    assertThat(image1.getSequence()).isEqualTo(1);
    assertThat(image2.getSequence()).isEqualTo(4);
    assertThat(image3.getSequence()).isEqualTo(2);
    assertThat(image4.getSequence()).isEqualTo(3);
    assertThat(image5.getSequence()).isEqualTo(5);
  }

  @Test
  @DisplayName("문의 이미지 삭제")
  void deleteImageSequence() throws Exception {
    // given
    User user = User.builder().id(1L).email("test").build();
    CustomUserDetails customUserDetails = new CustomUserDetails(user);

    Inquiry inquiry = Inquiry.builder().id(1L).user(user).build();

    InquiryImage image1 = InquiryImage.builder().id(1L).inquiry(inquiry).url("1").sequence(1).build();
    InquiryImage image2 = InquiryImage.builder().id(2L).inquiry(inquiry).url("2").sequence(2).build();
    InquiryImage image3 = InquiryImage.builder().id(3L).inquiry(inquiry).url("3").sequence(3).build();
    InquiryImage image4 = InquiryImage.builder().id(4L).inquiry(inquiry).url("4").sequence(4).build();
    InquiryImage image5 = InquiryImage.builder().id(5L).inquiry(inquiry).url("5").sequence(5).build();
    List<InquiryImage> imageList = new ArrayList<>();
    imageList.add(image1);
    imageList.add(image2);
    imageList.add(image3);
    imageList.add(image4);
    imageList.add(image5);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(inquiryRepository.findById(1L)).thenReturn(Optional.of(inquiry));
//    when(inquiryImageRepository.findById(1L)).thenReturn(Optional.of(image1));
    when(inquiryImageRepository.findById(2L)).thenReturn(Optional.of(image2));
//    when(inquiryImageRepository.findById(3L)).thenReturn(Optional.of(image3));
//    when(inquiryImageRepository.findById(4L)).thenReturn(Optional.of(image4));
//    when(inquiryImageRepository.findById(5L)).thenReturn(Optional.of(image5));
    when(inquiryImageRepository.findAllByInquiry(any())).thenReturn(imageList);

    // when
    inquiryService.deleteImage(customUserDetails.getId(), 1L, 2L);

    // then
    verify(inquiryImageRepository, times(1)).delete(any());
    assertThat(image1.getSequence()).isEqualTo(1);
    assertThat(image3.getSequence()).isEqualTo(2);
    assertThat(image4.getSequence()).isEqualTo(3);
    assertThat(image5.getSequence()).isEqualTo(4);
  }

}