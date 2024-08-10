package com.zerobase.babdeusilbun.meeting.service;

import static org.assertj.core.api.Assertions.*;

import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MeetingServiceTest {

  @Autowired
  private MeetingRepository meetingRepository;

  @Autowired
  private MeetingService meetingService;
  @Autowired
  private StoreRepository storeRepository;

  @BeforeEach
  void init() {

  }

  @Test
  @DisplayName("모임 조회 - 결제 마감 시간 순")
  void searchMeetingByDeadline() {

    PageRequest pageRequest = PageRequest.of(0, 4);

    Page<MeetingDto> pageable =
        meetingService.getAllMeetingList(1L, "deadline", pageRequest);

    List<MeetingDto> content = pageable.getContent();

    assertThat(pageable.getSize()).isEqualTo(4);
    assertThat(pageable.getTotalElements()).isEqualTo(3);
    assertThat(pageable.getTotalPages()).isEqualTo(1);
    assertThat(content.size()).isEqualTo(3);

    assertThat(content.getFirst().getPaymentAvailableAt()).isEqualTo(
        LocalDateTime.of(2024, Month.AUGUST, 24, 12, 0)
    );
    assertThat(content.getLast().getPaymentAvailableAt()).isEqualTo(
        LocalDateTime.of(2024, Month.AUGUST, 24, 12, 20)
    );
  }

  @Test
  @DisplayName("모임 조회 - 배송시간 시간 순")
  void searchMeetingByShippingTime() {

    PageRequest pageRequest = PageRequest.of(0, 4);

    Page<MeetingDto> pageable =
        meetingService.getAllMeetingList(1L, "shipping-time", pageRequest);

    List<MeetingDto> content = pageable.getContent();

    assertThat(pageable.getSize()).isEqualTo(4);
    assertThat(pageable.getTotalElements()).isEqualTo(3);
    assertThat(pageable.getTotalPages()).isEqualTo(1);
    assertThat(content.size()).isEqualTo(3);

    assertThat(content.getFirst().getDeliveredAt()).isEqualTo(
        LocalDateTime.of(2024, Month.AUGUST, 24, 12, 0)
    );
    assertThat(content.getLast().getDeliveredAt()).isEqualTo(
        LocalDateTime.of(2024, Month.AUGUST, 24, 12, 20)
    );
  }

  @Test
  @DisplayName("모임 조회 - 배달비 순")
  void searchMeetingByShippingFee() {

    PageRequest pageRequest = PageRequest.of(0, 4);

    Page<MeetingDto> pageable =
        meetingService.getAllMeetingList(1L, "shipping-fee", pageRequest);

    List<MeetingDto> content = pageable.getContent();

    assertThat(pageable.getSize()).isEqualTo(4);
    assertThat(pageable.getTotalElements()).isEqualTo(3);
    assertThat(pageable.getTotalPages()).isEqualTo(1);
    assertThat(content.size()).isEqualTo(3);

    assertThat(content.getFirst().getDeliveryFee()).isEqualTo(3000);
    assertThat(content.getLast().getDeliveryFee()).isEqualTo(4000);
  }

  @Test
  @DisplayName("모임 조회 - 최소 주문금액 순")
  void searchMeetingByMinPrice() {

    PageRequest pageRequest = PageRequest.of(0, 4);

    Page<MeetingDto> pageable =
        meetingService.getAllMeetingList(1L, "min-price", pageRequest);

    List<MeetingDto> content = pageable.getContent();
    Long storeAId = content.getLast().getStoreId();
    Long storeBId = content.getFirst().getStoreId();
    Store findStoreA = storeRepository.findById(storeBId).get();
    Store findStoreB = storeRepository.findById(storeAId).get();

    assertThat(pageable.getSize()).isEqualTo(4);
    assertThat(pageable.getTotalElements()).isEqualTo(3);
    assertThat(pageable.getTotalPages()).isEqualTo(1);
    assertThat(content.size()).isEqualTo(3);

    assertThat(findStoreA.getMinOrderAmount()).isEqualTo(1000L);
    assertThat(findStoreB.getMinOrderAmount()).isEqualTo(2000L);
  }



}