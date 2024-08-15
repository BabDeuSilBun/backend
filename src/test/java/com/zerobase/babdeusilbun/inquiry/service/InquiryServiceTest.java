package com.zerobase.babdeusilbun.inquiry.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.domain.Inquiry;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.enums.InquiryStatus;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.DetailResponse;
import com.zerobase.babdeusilbun.inquiry.dto.InquiryDto.ListResponse;
import com.zerobase.babdeusilbun.inquiry.service.impl.InquiryServiceImpl;
import com.zerobase.babdeusilbun.repository.InquiryRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
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

    when(inquiryRepository.findInquiryList(anyString(), any())).thenReturn(list);

    // when
    Page<ListResponse> inquiryList = inquiryService.getInquiryList("", pageable);
    List<ListResponse> content = inquiryList.getContent();

    // then
    assertThat(inquiryList.getTotalPages()).isEqualTo(1);
    assertThat(inquiryList.getSize()).isEqualTo(1);
    assertThat(inquiryList.getTotalElements()).isEqualTo(1);
    assertThat(content.size()).isEqualTo(1);
    assertThat(content.getFirst().getTitle()).isEqualTo("title1");
    assertThat(content.getFirst().getStatus()).isEqualTo(InquiryStatus.PENDING);
  }

  @Test
  @DisplayName("문의 정보 조회")
  void getInquiryInfo() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Inquiry pending = Inquiry.builder()
        .id(1L)
        .user(user).title("title1").content("content1")
        .status(InquiryStatus.PENDING).build();

    when(inquiryRepository.findById(anyLong())).thenReturn(Optional.of(pending));

    // when
    DetailResponse inquiryInfo = inquiryService.getInquiryInfo(1L);

    // then
    assertThat(inquiryInfo.getInquiryId()).isEqualTo(1L);
    assertThat(inquiryInfo.getTitle()).isEqualTo(pending.getTitle());
    assertThat(inquiryInfo.getAnswer()).isEqualTo(pending.getAnswer());
    assertThat(inquiryInfo.getContent()).isEqualTo(pending.getContent());
    assertThat(inquiryInfo.getStatus()).isEqualTo(pending.getStatus());
    assertThat(inquiryInfo.getCreatedAt()).isEqualTo(pending.getCreatedAt());
    assertThat(inquiryInfo.getUpdatedAt()).isEqualTo(pending.getUpdatedAt());
  }

}