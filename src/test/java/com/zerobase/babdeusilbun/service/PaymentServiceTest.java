package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.enums.MeetingStatus.*;
import static com.zerobase.babdeusilbun.enums.PaymentMethod.KAKAOPAY;
import static com.zerobase.babdeusilbun.enums.PurchaseStatus.*;
import static com.zerobase.babdeusilbun.enums.PurchaseType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.zerobase.babdeusilbun.domain.Meeting;
import com.zerobase.babdeusilbun.domain.Menu;
import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.domain.Purchase;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.TeamPurchase;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.PaymentDto.ConfirmRequest;
import com.zerobase.babdeusilbun.dto.PaymentDto.ConfirmResponse;
import com.zerobase.babdeusilbun.dto.PaymentDto.ProcessRequest;
import com.zerobase.babdeusilbun.dto.PaymentDto.ProcessResponse;
import com.zerobase.babdeusilbun.dto.PaymentDto.Temporary;
import com.zerobase.babdeusilbun.enums.PaymentGateway;
import com.zerobase.babdeusilbun.enums.PointType;
import com.zerobase.babdeusilbun.repository.IndividualPurchaseRepository;
import com.zerobase.babdeusilbun.repository.MeetingRepository;
import com.zerobase.babdeusilbun.repository.PaymentRepository;
import com.zerobase.babdeusilbun.repository.PointRepository;
import com.zerobase.babdeusilbun.repository.PurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.PurchaseRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchasePaymentRepository;
import com.zerobase.babdeusilbun.repository.TeamPurchaseRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.impl.PaymentServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.RedissonLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

  @InjectMocks
  private PaymentServiceImpl paymentService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private StoreRepository storeRepository;
  @Mock
  private MeetingRepository meetingRepository;
  @Mock
  private PaymentRepository paymentRepository;
  @Mock
  private PurchaseRepository purchaseRepository;
  @Mock
  private PurchasePaymentRepository purchasePaymentRepository;
  @Mock
  private TeamPurchaseRepository teamPurchaseRepository;
  @Mock
  private TeamPurchasePaymentRepository teamPurchasePaymentRepository;
  @Mock
  private IndividualPurchaseRepository individualPurchaseRepository;
  @Mock
  private PointRepository pointRepository;

  @Mock
  private RedissonClient redissonClient;

  @Mock
  private IamportClient iamportClient;

  @Test
  @DisplayName("모임장, 모임원의 결제 진행 요청")
  void requestPayment() throws Exception {
    // given
    User user = User.builder().id(1L).point(1000L).build();
    Store store = Store.builder().id(1L).deliveryPrice(1000L).build();
    Meeting meeting = Meeting.builder().id(1L)
        .store(store)
        .purchaseType(DINING_TOGETHER).status(GATHERING).minHeadcount(3)
        .build();
    Purchase purchase = Purchase.builder().id(1L)
        .meeting(meeting).status(PRE_PURCHASE)
        .build();
    Menu menu1 = Menu.builder().id(1L).name("menu1").price(1000L).build();
    Menu menu2 = Menu.builder().id(2L).name("menu2").price(2000L).build();
    TeamPurchase teamPurchase1 = TeamPurchase.builder()
        .menu(menu1).quantity(5).meeting(meeting)
        .build();
    TeamPurchase teamPurchase2 = TeamPurchase.builder()
        .menu(menu2).quantity(1).meeting(meeting)
        .build();

    ProcessRequest processRequest = ProcessRequest.builder().point(200L).payMethod(KAKAOPAY)
        .build();

    RLock lock = Mockito.mock(RedissonLock.class);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findWithStoreById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.findById(1L)).thenReturn(Optional.of(purchase));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
    when(teamPurchaseRepository.findAllByMeeting(meeting))
        .thenReturn(List.of(teamPurchase1, teamPurchase2));
    when(redissonClient.getLock(anyString())).thenReturn(lock);
    when(lock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);

    // when
    ProcessResponse processResponse = paymentService.requestPayment(1L, 1L, 1L, processRequest);

    // then
    assertThat(user.getPoint()).isEqualTo(800L);
    assertThat(processResponse.getName()).isEqualTo("menu1 외 1건");
    assertThat(processResponse.getPrice()).isEqualTo(2460L);
  }

  @Test
  @DisplayName("결제 진행 후 결제 성공 확인 요청")
  void confirmPayment() throws Exception {
    // given
    User user = User.builder().id(1L).point(1000L).build();
    Store store = Store.builder().id(1L).deliveryPrice(1000L).build();
    Meeting meeting = Meeting.builder().id(1L)
        .store(store)
        .purchaseType(DINING_TOGETHER).status(GATHERING).minHeadcount(3)
        .build();
    Purchase purchase = Purchase.builder().id(1L)
        .meeting(meeting).status(PRE_PURCHASE)
        .build();
    Point point = Point.builder().id(1L).amount(1000L).type(PointType.MINUS).content("name")
        .user(user).build();

    Temporary temporary = Temporary.builder()
        .pg(PaymentGateway.KAKAOPAY)
        .point(1000L)
        .transactionId("transaction")
        .payMethod(KAKAOPAY)
        .price(10000L)
        .name("name")
        .build();

    ConfirmRequest request =
        ConfirmRequest.builder().portoneUid("port").transactionId("transaction").build();


    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(meetingRepository.findWithStoreById(1L)).thenReturn(Optional.of(meeting));
    when(purchaseRepository.findById(1L)).thenReturn(Optional.of(purchase));
    when(purchaseRepository.existsByMeetingAndUser(meeting, user)).thenReturn(true);
    when(pointRepository.save(any())).thenReturn(point);

    IamportResponse response = Mockito.mock(IamportResponse.class);
    Payment payment = Mockito.mock(Payment.class);

    when(iamportClient.paymentByImpUid(request.getPortoneUid())).thenReturn(response);
    when(response.getCode()).thenReturn(0);
    when(response.getResponse()).thenReturn(payment);
    when(payment.getPayMethod()).thenReturn("kakaopay");
    when(payment.getPgProvider()).thenReturn("kakaopay");
    when(payment.getStatus()).thenReturn("paid");
    when(payment.getAmount()).thenReturn(BigDecimal.valueOf(10000));
    when(payment.getName()).thenReturn("name");
//    when(payment.getImpUid()).thenReturn("port");

    // when
    ConfirmResponse confirmResponse =
        paymentService.confirmPayment(1L, 1L, 1L, request, temporary);

    // then
    assertThat(confirmResponse.getSuccess()).isTrue();
    assertThat(confirmResponse.getTransactionId()).isEqualTo(request.getTransactionId());
  }


}