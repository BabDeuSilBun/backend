package com.zerobase.babdeusilbun.service;

import static com.zerobase.babdeusilbun.enums.PointSortCriteria.*;
import static com.zerobase.babdeusilbun.enums.PointType.*;
import static com.zerobase.babdeusilbun.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.zerobase.babdeusilbun.domain.Point;
import com.zerobase.babdeusilbun.domain.Store;
import com.zerobase.babdeusilbun.domain.User;
import com.zerobase.babdeusilbun.dto.PointDto.Response;
import com.zerobase.babdeusilbun.dto.PointDto.WithdrawalRequest;
import com.zerobase.babdeusilbun.enums.PointSortCriteria;
import com.zerobase.babdeusilbun.enums.PointType;
import com.zerobase.babdeusilbun.exception.CustomException;
import com.zerobase.babdeusilbun.exception.ErrorCode;
import com.zerobase.babdeusilbun.repository.PointRepository;
import com.zerobase.babdeusilbun.repository.StoreRepository;
import com.zerobase.babdeusilbun.repository.UserRepository;
import com.zerobase.babdeusilbun.service.impl.PointServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

  @InjectMocks
  private PointServiceImpl pointService;
  @Mock
  private PointRepository pointRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private StoreRepository storeRepository;

  @Captor
  private ArgumentCaptor<Point> pointArgumentCaptor;

  @Test
  @DisplayName("모든 포인트 내역 조회")
  void getAllPointList() throws Exception {
    // given
    User user = User.builder().id(1L).build();
    Store store = Store.builder().id(1L).build();
    Point point1 = getPoint(user, 1, MINUS);
    Point point2 = getPoint(user, 2, PLUS);
    Point point3 = getPoint(user, 3, MINUS);
    Point point4 = getPoint(user, 4, PLUS);

    Pageable pageable = PageRequest.of(0, 2);

    List<Point> list = List.of(point1, point3);
    Page<Point> page = new PageImpl<>(list, pageable, 2);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(storeRepository.findStoreByPoint(point1)).thenReturn(Optional.of(store));
//    when(storeRepository.findStoreByPoint(point2)).thenReturn(Optional.of(store));
    when(storeRepository.findStoreByPoint(point3)).thenReturn(Optional.of(store));
//    when(storeRepository.findStoreByPoint(point4)).thenReturn(Optional.of(store));
    when(pointRepository.findSortedAllByUser(user, List.of(USE.getPointType()), pageable))
        .thenReturn(page);

    // when
    Page<Response> result = pointService.getAllPointList(1L, pageable, "use");
    List<Response> content = result.getContent();

    // then
    assertThat(result.getTotalPages()).isEqualTo(1);
    assertThat(content.getFirst().getAmount()).isEqualTo(1000);
    assertThat(content.get(1).getAmount()).isEqualTo(3000);
  }

  @Test
  @DisplayName("포인트 인출 - 성공")
  void withdrawalPoint() throws Exception {
    // given
    User user = User.builder().id(1L).point(1000L).build();
    WithdrawalRequest request = WithdrawalRequest.builder().amount(100).build();
    Point point = Point.builder()
        .id(1L)
        .user(user).type(MINUS).amount(request.getAmount().longValue()).content("포인트 인출")
        .build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(pointRepository.save(any())).thenReturn(point);

    // when
    Point result = pointService.withdrawalPoint(1L, request);

    // then
    verify(pointRepository).save(pointArgumentCaptor.capture());
    Point captor = pointArgumentCaptor.getValue();
    assertThat(captor.getUser()).isEqualTo(user);
    assertThat(captor.getType()).isEqualTo(MINUS);
    assertThat(captor.getAmount()).isEqualTo(request.getAmount().longValue());
    assertThat(captor.getContent()).isEqualTo("포인트 인출");
    assertThat(user.getPoint()).isEqualTo(900L);
  }

  @Test
  @DisplayName("포인트 인출 - 실패 - 잔액 부족")
  void failWithdrawalPoint() throws Exception {
    // given
    User user = User.builder().id(1L).point(100L).build();
    WithdrawalRequest request = WithdrawalRequest.builder().amount(101).build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    // when
    CustomException customException = assertThrows(CustomException.class,
        () -> pointService.withdrawalPoint(1L, request));

    // then
    assertThat(customException.getErrorCode()).isEqualTo(POINT_SHORTAGE);
    assertThat(user.getPoint()).isEqualTo(100L);
  }

  private Point getPoint(User user, Integer i, PointType type) {
    return Point.builder()
        .id(i.longValue()).user(user).type(type).amount(i * 1000L)
        .build();
  }

}