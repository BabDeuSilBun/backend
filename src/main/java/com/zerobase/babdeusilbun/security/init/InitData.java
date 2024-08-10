package com.zerobase.babdeusilbun.security.init;

import static com.zerobase.babdeusilbun.enums.MeetingStatus.GATHERING;
import static com.zerobase.babdeusilbun.enums.MeetingStatus.MEETING_CANCELLED;
import static com.zerobase.babdeusilbun.enums.MeetingStatus.ORDER_CANCELLED;
import static com.zerobase.babdeusilbun.enums.PurchaseType.DELIVERY_TOGETHER;

import com.zerobase.babdeusilbun.domain.Address;
import com.zerobase.babdeusilbun.domain.Entrepreneur;
import com.zerobase.babdeusilbun.domain.Major;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.School;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.StoreImage;
import com.zerobase.babdeusilbun.domain.StoreSchool;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.enums.MeetingStatus;
import com.zerobase.babdeusilbun.enums.PurchaseType;
import com.zerobase.babdeusilbun.repository.EntrepreneurRepository;
import com.zerobase.babdeusilbun.repository.MajorRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.SchoolRepository;
import com.zerobase.babdeusilbun.repository.StoreImageRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.repository.StoreSchoolRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * 테스트용 더미 데이터
 */
@Profile("test")
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@Configuration
@RequiredArgsConstructor
public class InitData {

  private final UserRepository userRepository;
  private final EntrepreneurRepository entrepreneurRepository;
  private final MeetingRepository meetingRepository;
  private final SchoolRepository schoolRepository;
  private final MajorRepository majorRepository;
  private final StoreRepository storeRepository;
  private final StoreImageRepository storeImageRepository;
  private final StoreSchoolRepository storeSchoolRepository;

  @Bean
  public CommandLineRunner loadData() {

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

      Store storeA = getTestStore("storeA", savedEntrepreneurA, 3000, 2000L);
      Store storeB = getTestStore("storeB", savedEntrepreneurA, 4000, 1000L);
      Store savedStoreA = storeRepository.save(storeA);
      Store savedStoreB = storeRepository.save(storeB);

      StoreSchool storeSchoolA = StoreSchool.builder().store(savedStoreA).school(savedSchoolA).build();
      StoreSchool storeSchoolB = StoreSchool.builder().store(savedStoreB).school(savedSchoolA).build();
      StoreSchool savedStoreSchoolA = storeSchoolRepository.save(storeSchoolA);
      StoreSchool savedStoreSchoolB = storeSchoolRepository.save(storeSchoolB);

      StoreImage storeImage1 = getStoreImage(savedStoreA, true, 1);
      StoreImage storeImage2 = getStoreImage(savedStoreA, false, 2);
      StoreImage storeImage3 = getStoreImage(savedStoreA, false, 3);
      StoreImage savedImage1 = storeImageRepository.save(storeImage1);
      StoreImage savedImage2 = storeImageRepository.save(storeImage2);
      StoreImage savedImage3 = storeImageRepository.save(storeImage3);

      Meeting meetingA = getTestMeeting(savedUserA, savedStoreA, DELIVERY_TOGETHER, GATHERING,
          LocalDateTime.of(2024, Month.AUGUST, 24, 12,10));
      Meeting meetingB = getTestMeeting(savedUserA, savedStoreB, DELIVERY_TOGETHER, ORDER_CANCELLED,
          LocalDateTime.of(2024, Month.AUGUST, 24, 12,0));
      Meeting meetingC = getTestMeeting(savedUserA, savedStoreA, DELIVERY_TOGETHER, MEETING_CANCELLED,
          LocalDateTime.of(2024, Month.AUGUST, 24, 12,20));
      Meeting savedMeetingA = meetingRepository.save(meetingA);
      Meeting savedMeetingB = meetingRepository.save(meetingB);
      Meeting savedMeetingC = meetingRepository.save(meetingC);


    };
  }

  private StoreImage getStoreImage(Store savedStoreA, boolean isRepresentative, int sequence) {
    return StoreImage.builder().store(savedStoreA).url("testurl")
        .isRepresentative(isRepresentative).sequence(sequence).build();
  }

  private Meeting getTestMeeting(User savedUserA, Store savedStoreA, PurchaseType purchaseType,
      MeetingStatus meetingStatus, LocalDateTime availableDt) {
    return Meeting.builder()
        .leader(savedUserA)
        .store(savedStoreA)
        .purchaseType(purchaseType)
        .minHeadcount(1)
        .maxHeadcount(10)
        .isEarlyPaymentAvailable(true)
        .paymentAvailableDt(availableDt)
        .deliveredAddress(getTestAddress())
        .metAddress(getTestAddress())
        .deliveredAt(availableDt)
        .status(meetingStatus)
        .build();
  }

  private Store getTestStore(String storeName, Entrepreneur entrepreneur, int deliveryPrice,
      long minOrderAmount) {
    return Store.builder()
        .entrepreneur(entrepreneur)
        .name(storeName)
        .description("test store description")
        .minOrderAmount(minOrderAmount)
        .deliveryPrice(deliveryPrice)
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
