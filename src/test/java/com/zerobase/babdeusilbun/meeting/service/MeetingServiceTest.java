package com.zerobase.babdeusilbun.meeting.service;

import static com.zerobase.babdeusilbun.enums.MeetingStatus.GATHERING;
import static com.zerobase.babdeusilbun.enums.MeetingStatus.MEETING_CANCELLED;
import static com.zerobase.babdeusilbun.enums.MeetingStatus.PURCHASE_COMPLETED;
import static com.zerobase.babdeusilbun.enums.PurchaseType.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_PARTICIPANT_EXIST;
import static com.zerobase.babdeusilbun.exception.ErrorCode.MEETING_STATUS_INVALID;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.babdeusilbun.domain.Address;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.DeliveryAddressDto;
import com.zerobase.babdeusilbun.dto.MeetingDto;
import com.zerobase.babdeusilbun.dto.MetAddressDto;
import com.zerobase.babdeusilbun.enums.PurchaseStatus;
import com.zerobase.babdeusilbun.enums.PurchaseType;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.meeting.dto.MeetingRequest;
import com.zerobase.babdeusilbun.meeting.dto.MeetingRequest.Create;
import com.zerobase.babdeusilbun.meeting.scheduler.MeetingScheduler;
import com.zerobase.babdeusilbun.meeting.scheduler.MeetingSchedulerService;
import com.zerobase.babdeusilbun.meeting.service.impl.MeetingServiceImpl;
import com.zerobase.babdeusilbun.repository.MeetingQueryRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.StoreImageRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class MeetingServiceTest {

  @InjectMocks
  private MeetingServiceImpl meetingService;

  @Mock
  private MeetingScheduler meetingScheduler;

  @Mock
  private MeetingRepository meetingRepository;

  @Mock
  private MeetingQueryRepository meetingQueryRepository;

  @Mock
  private StoreImageRepository storeImageRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private StoreRepository storeRepository;

  @Mock
  private PurchaseRepository purchaseRepository;

  @Mock
  private UserDetails userDetails;


  @Test
  @DisplayName("모임 정보 조회 - 성공 - 페이징")
  void getAllMeetingList_ShouldReturnPageOfMeetingDtos() {
    // Given
    Long schoolId = 1L;
    String sortCriteria = "name";
    String searchMenu = "pizza";
    Long categoryFilter = 1L;
    Pageable pageable = mock(Pageable.class);

    User user = User.builder().email("test@example.com").build();
    Store store = Store.builder().id(1L).name("Test Store").build();
    Meeting meeting = Meeting.builder()
        .id(1L)
        .leader(user)
        .store(store)
        .purchaseType(DELIVERY_TOGETHER)
        .minHeadcount(2)
        .maxHeadcount(5)
        .isEarlyPaymentAvailable(true)
        .paymentAvailableDt(LocalDateTime.now().plusDays(1))
        .deliveredAddress(Address.builder().postal("").detailAddress("").streetAddress("").build())
        .metAddress(Address.builder().postal("").detailAddress("").streetAddress("").build())
        .status(GATHERING)
        .build();
    Page<Meeting> meetings = new PageImpl<>(List.of(meeting));

    when(meetingQueryRepository.findFilteredMeetingList(schoolId, sortCriteria, searchMenu,
        categoryFilter, pageable))
        .thenReturn(meetings);
    when(storeImageRepository.findAllByStoreOrderBySequenceAsc(store))
        .thenReturn(Collections.emptyList());

    // When
    Page<MeetingDto> result = meetingService.getAllMeetingList(schoolId, sortCriteria, searchMenu,
        categoryFilter, pageable);

    // Then
    assertEquals(1, result.getTotalElements());
    verify(meetingQueryRepository, times(1)).findFilteredMeetingList(schoolId, sortCriteria,
        searchMenu, categoryFilter, pageable);
  }

  @Test
  @DisplayName("모임 정보 조회 - 성공")
  void getMeetingInfo_ShouldReturnMeetingDto() {
    // Given
    Long meetingId = 1L;

    User user = User.builder().email("test@example.com").build();
    Store store = Store.builder().id(1L).name("Test Store").build();
    Meeting meeting = Meeting.builder()
        .id(1L)
        .leader(user)
        .store(store)
        .purchaseType(PurchaseType.DELIVERY_TOGETHER)
        .minHeadcount(2)
        .maxHeadcount(5)
        .isEarlyPaymentAvailable(true)
        .paymentAvailableDt(LocalDateTime.now().plusDays(1))
        .deliveredAddress(Address.builder().postal("").detailAddress("").streetAddress("").build())
        .metAddress(Address.builder().postal("").detailAddress("").streetAddress("").build())
        .status(GATHERING)
        .build();

    when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
    when(storeImageRepository.findAllByStoreOrderBySequenceAsc(store)).thenReturn(
        Collections.emptyList());

    // When
    MeetingDto result = meetingService.getMeetingInfo(meetingId);

    // Then
    assertNotNull(result);
    assertEquals(meetingId, result.getMeetingId());
    verify(meetingRepository, times(1)).findById(meetingId);
  }

  @Test
  @DisplayName("모임 생성 성공")
  void createMeeting_ShouldSaveMeetingAndPurchase() {
    // Given
    MeetingRequest.Create request = Create.builder()
        .storeId(1L)
        .minHeadcount(1)
        .maxHeadcount(10)
        .deliveryAddress(
            DeliveryAddressDto.builder().deliveryPostal("").deliveryDetailAddress("")
                .deliveryStreetAddress("").build())
        .metAddress(
            MetAddressDto.builder().metPostal("").metDetailAddress("").metStreetAddress("").build())
        .build();

    User user = User.builder().email("test@example.com").build();
    Store store = Store.builder().id(1L).name("Test Store").build();
    Meeting meeting = Meeting.builder()
        .id(1L)
        .leader(user)
        .store(store)
        .purchaseType(PurchaseType.DELIVERY_TOGETHER)
        .minHeadcount(2)
        .maxHeadcount(5)
        .isEarlyPaymentAvailable(true)
        .paymentAvailableDt(LocalDateTime.now().plusDays(1))
        .status(GATHERING)
        .deliveredAddress(Address.builder().postal("").detailAddress("").streetAddress("").build())
        .metAddress(Address.builder().postal("").detailAddress("").streetAddress("").build())
        .build();

    when(userDetails.getUsername()).thenReturn(user.getEmail());
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    when(storeRepository.findById(any(Long.class))).thenReturn(Optional.of(store));
    when(meetingRepository.save(any(Meeting.class))).thenReturn(meeting);

    // When
    meetingService.createMeeting(request, userDetails);

    // Then
    verify(meetingRepository, times(1)).save(any(Meeting.class));
    verify(purchaseRepository, times(1)).save(any(Purchase.class));
  }

  @Test
  @DisplayName("모임 수정 성공")
  void updateMeeting_ShouldUpdateMeetingDetails() {
    // Given
    Long meetingId = 1L;
    MeetingRequest.Update request = MeetingRequest.Update.builder()
        .maxHeadcount(10)
        .deliveryAddress(
            DeliveryAddressDto.builder().deliveryPostal("update").deliveryDetailAddress("update")
                .deliveryStreetAddress("update").build())
        .metAddress(MetAddressDto.builder().metPostal("update").metDetailAddress("update")
            .metStreetAddress("update").build())
        .build();

    User user = User.builder().email("test@example.com").build();
    Store store = Store.builder().id(1L).name("Test Store").build();
    Meeting meeting = Meeting.builder()
        .id(1L)
        .leader(user)
        .store(store)
        .purchaseType(PurchaseType.DELIVERY_TOGETHER)
        .minHeadcount(2)
        .maxHeadcount(5)
        .isEarlyPaymentAvailable(true)
        .paymentAvailableDt(LocalDateTime.now().plusDays(1))
        .status(GATHERING)
        .build();

    when(userDetails.getUsername()).thenReturn(user.getEmail());
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));

    // When
    meetingService.updateMeeting(meetingId, request, userDetails);

    // Then
    verify(meetingRepository, times(1)).findById(meetingId);
  }

  @Test
  @DisplayName("모임 취소 성공 - 모임장")
  void success_meeting_withdrawal_leader() throws Exception {
    // given

    UserDetails userDetails = new org.springframework.security.core.userdetails
        .User("leader", "", List.of(new SimpleGrantedAuthority("user")));

    User leader = User.builder().email("leader").build();
    Meeting meeting = Meeting.builder()
        .leader(leader)
        .status(GATHERING)
        .build();

    when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meeting));
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(leader));
    when(purchaseRepository.findAllByMeeting(any())).thenReturn(List.of(new Purchase()));


    // when
    meetingService.withdrawMeeting(1L, userDetails);

    // then
    assertThat(meeting.getDeletedAt()).isNotNull();
    assertThat(meeting.getStatus()).isEqualTo(MEETING_CANCELLED);
  }

  @Test
  @DisplayName("모임 취소 실패 - 모임장 - 모임원 존재")
  void fail_meeting_withdrawal_leader_existParticipant() throws Exception {
    // given

    UserDetails userDetails = new org.springframework.security.core.userdetails
        .User("leader", "", List.of(new SimpleGrantedAuthority("user")));

    User leader = User.builder().email("leader").build();
    Meeting meeting = Meeting.builder()
        .leader(leader)
        .status(GATHERING)
        .build();

    when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meeting));
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(leader));
    when(purchaseRepository.findAllByMeeting(any())).thenReturn(List.of(new Purchase(), new Purchase()));


    // when
    CustomException customException = assertThrows
        (CustomException.class, () -> meetingService.withdrawMeeting(1L, userDetails));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_PARTICIPANT_EXIST);
    assertThat(meeting.getDeletedAt()).isNull();
    assertThat(meeting.getStatus()).isEqualTo(GATHERING);
  }

  @Test
  @DisplayName("모임 취소 실패 - 모임장 - 모집중 아님")
  void fail_meeting_withdrawal_leader_not_gathering() throws Exception {
    // given

    UserDetails userDetails = new org.springframework.security.core.userdetails
        .User("leader", "", List.of(new SimpleGrantedAuthority("user")));

    User leader = User.builder().email("leader").build();
    Meeting meeting = Meeting.builder()
        .leader(leader)
        .status(PURCHASE_COMPLETED)
        .build();

    when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meeting));
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(leader));

    // when
    CustomException customException = assertThrows
        (CustomException.class, () -> meetingService.withdrawMeeting(1L, userDetails));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_STATUS_INVALID);
    assertThat(meeting.getDeletedAt()).isNull();
    assertThat(meeting.getStatus()).isEqualTo(PURCHASE_COMPLETED);
  }

  @Test
  @DisplayName("모임 취소 성공 - 모임원")
  void success_meeting_withdrawal_user() throws Exception {
    // given

    UserDetails userDetails = new org.springframework.security.core.userdetails
        .User("user", "", List.of(new SimpleGrantedAuthority("user")));

    User leader = User.builder().email("leader").build();
    User user = User.builder().email("user").build();
    Meeting meeting = Meeting.builder()
        .leader(leader)
        .status(GATHERING)
        .build();

    Purchase purchase = Purchase.builder().meeting(meeting).status(PurchaseStatus.PROGRESS).build();

    when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meeting));
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
    when(purchaseRepository.findByMeetingAndUser(any(), any())).thenReturn(Optional.of(purchase));


    // when
    meetingService.withdrawMeeting(1L, userDetails);

    // then
    assertThat(meeting.getDeletedAt()).isNull();
    assertThat(meeting.getStatus()).isEqualTo(GATHERING);
    assertThat(purchase.getStatus()).isEqualTo(PurchaseStatus.CANCEL);
  }

  @Test
  @DisplayName("모임 취소 실패 - 모임원 - 모집중 아님")
  void fail_meeting_withdrawal_user_not_gathering() throws Exception {
    // given

    UserDetails userDetails = new org.springframework.security.core.userdetails
        .User("user", "", List.of(new SimpleGrantedAuthority("user")));

    User leader = User.builder().email("leader").build();
    User user = User.builder().email("user").build();
    Meeting meeting = Meeting.builder()
        .leader(leader)
        .status(PURCHASE_COMPLETED)
        .build();

    when(meetingRepository.findById(anyLong())).thenReturn(Optional.of(meeting));
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

    // when
    CustomException customException = assertThrows
        (CustomException.class, () -> meetingService.withdrawMeeting(1L, userDetails));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(MEETING_STATUS_INVALID);
    assertThat(meeting.getDeletedAt()).isNull();
    assertThat(meeting.getStatus()).isEqualTo(PURCHASE_COMPLETED);
  }



}









