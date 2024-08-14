package com.zerobase.babdeusilbun.repository.custom.impl;

import static com.zerobase.babdeusilbun.domain.QInquiry.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.babdeusilbun.domain.Inquiry;
import com.zerobase.babdeusilbun.enums.InquiryStatus;
import com.zerobase.babdeusilbun.repository.custom.CustomInquiryRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class CustomInquiryRepositoryImpl implements CustomInquiryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Inquiry> findInquiryList(String statusFilter, Pageable pageable) {

    List<Inquiry> inquiryList = queryFactory.selectFrom(inquiry)
        .where(where(statusFilter))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long count = queryFactory.select(inquiry.id.count())
        .where(where(statusFilter))
        .fetchOne();

    return new PageImpl<>(inquiryList, pageable, count);
  }

  private BooleanExpression[] where(String statusFilter) {
    List<BooleanExpression> list = new ArrayList<>();

    if (!StringUtils.hasText(statusFilter)) {
      return list.toArray(new BooleanExpression[0]);
    }

    list.add(whereStatusFilter(statusFilter));

    return list.toArray(new BooleanExpression[0]);
  }

  private BooleanExpression whereStatusFilter(String statusFilter) {
    return inquiry.status.eq(InquiryStatus.valueOf(statusFilter));
  }

}
