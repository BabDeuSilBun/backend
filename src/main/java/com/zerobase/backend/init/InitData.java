package com.zerobase.backend.init;

import static com.zerobase.backend.enums.MeetingStatus.GATHERING;
import static com.zerobase.backend.enums.MeetingStatus.MEETING_CANCELLED;
import static com.zerobase.backend.enums.MeetingStatus.ORDER_CANCELLED;
import static com.zerobase.backend.enums.PurchaseType.DELIVERY_TOGETHER;

import com.zerobase.backend.domain.Address;
import com.zerobase.backend.domain.Entrepreneur;
import com.zerobase.backend.domain.Major;
import com.zerobase.backend.domain.Meeting;
import com.zerobase.backend.domain.School;
import com.zerobase.backend.domain.Store;
import com.zerobase.backend.domain.User;
import com.zerobase.backend.enums.MeetingStatus;
import com.zerobase.backend.enums.PurchaseType;
import com.zerobase.backend.repository.EntrepreneurRepository;
import com.zerobase.backend.repository.MajorRepository;
import com.zerobase.backend.repository.MeetingRepository;
import com.zerobase.backend.repository.SchoolRepository;
import com.zerobase.backend.repository.StoreRepository;
import com.zerobase.backend.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 테스트용 더미 데이터
 */
@Profile("test")
@Configuration
@RequiredArgsConstructor
public class InitData {

  private final SchoolRepository schoolRepository;
  private final MajorRepository majorRepository;

  @Bean
  public CommandLineRunner loadData(UserRepository userRepository,
      EntrepreneurRepository entrepreneurRepository, StoreRepository storeRepository,
      MeetingRepository meetingRepository) {

    return args -> {


      School schoolA = School.builder().name("schoolA").campus("campusA").build();
      School schoolB = School.builder().name("schoolB").campus("campusB").build();
      School savedSchoolA = schoolRepository.save(schoolA);
      School savedSchoolB = schoolRepository.save(schoolB);

      Major majorA = Major.builder().name("majorA").build();
      Major majorB = Major.builder().name("majorB").build();
      Major savedMajorA = majorRepository.save(majorA);
      Major savedMajorB = majorRepository.save(majorB);

      User userA = getTestUser(savedSchoolA, savedMajorA);
      Entrepreneur entrepreneurA = getTestEntrepreneur();
      User savedUserA = userRepository.save(userA);
      Entrepreneur savedEntrepreneurA = entrepreneurRepository.save(entrepreneurA);

      Store storeA = getTestStore(savedEntrepreneurA);
      Store savedStoreA = storeRepository.save(storeA);

      Meeting meetingA = getTestMeeting(savedUserA, savedStoreA, DELIVERY_TOGETHER, GATHERING);
      Meeting meetingB = getTestMeeting(savedUserA, savedStoreA, DELIVERY_TOGETHER, ORDER_CANCELLED);
      Meeting meetingC = getTestMeeting(savedUserA, savedStoreA, DELIVERY_TOGETHER, MEETING_CANCELLED);
      Meeting savedMeetingA = meetingRepository.save(meetingA);
      Meeting savedMeetingB = meetingRepository.save(meetingB);
      Meeting savedMeetingC = meetingRepository.save(meetingC);


    };
  }

  private Meeting getTestMeeting(User savedUserA, Store savedStoreA, PurchaseType purchaseType,
      MeetingStatus meetingStatus) {
    return Meeting.builder()
        .leader(savedUserA)
        .store(savedStoreA)
        .purchaseType(purchaseType)
        .minHeadcount(1)
        .maxHeadcount(10)
        .isEarlyPaymentAvailable(true)
        .paymentAvailableDt(LocalDateTime.now())
        .deliveredAddress(getTestAddress())
        .metAddress(getTestAddress())
        .status(meetingStatus)
        .build();
  }

  private Store getTestStore(Entrepreneur entrepreneur) {
    return Store.builder()
        .entrepreneur(entrepreneur)
        .name("test store name")
        .description("test store description")
        .minOrderAmount(10000L)
        .deliveryPrice(3000)
        .minDeliveryTime(10)
        .maxDeliveryTime(20)
        .address(getTestAddress())
        .phoneNumber("01000001111")
        .openTime(LocalTime.now())
        .closeTime(LocalTime.now())
        .build();
  }

  private Address getTestAddress() {
    return Address.builder()
        .postal("test postal")
        .streetAddress("test street address")
        .detailAddress("test detail address")
        .build();
  }

  private Entrepreneur getTestEntrepreneur() {
    return Entrepreneur.builder()
        .email("testentrepreneur@test.com")
        // password = 1234
        .password("$2a$10$f4Gb.emyVjK/Be/5nJPD9OjWhPNp6k/8J1SSgHxsQ7SJzQJgR3Wj.")
        .name("test entrepreneur name")
        .phoneNumber("01000000000")
        .businessNumber("11111111")
        .build();
  }

  private User getTestUser(School schoolA, Major majorA) {
    User userA = User.builder().school(schoolA).major(majorA)
        .email("testuser@test.com")
        // password = 1234
        .password("$2a$10$f4Gb.emyVjK/Be/5nJPD9OjWhPNp6k/8J1SSgHxsQ7SJzQJgR3Wj.")
        .phoneNumber("01000000000")
        .isBanned(false).name("test user name").nickname("testnickname").point(0L).build();
    return userA;
  }


}
