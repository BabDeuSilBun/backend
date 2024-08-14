package com.zerobase.babdeusilbun.repository.custom.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.babdeusilbun.domain.QSchool;
import com.zerobase.babdeusilbun.dto.QSchoolDto_Information;
import com.zerobase.babdeusilbun.dto.SchoolDto.Information;
import com.zerobase.babdeusilbun.repository.custom.CustomSchoolRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomSchoolRepositoryImpl implements CustomSchoolRepository {
  private final JPAQueryFactory jpaQueryFactory;

  private final QSchool school = QSchool.school;

  @Override
  public Page<Information> searchSchoolNameByKeywords(String[] keywords, int page, int size) {
    BooleanBuilder builder = new BooleanBuilder();

    for (String keyword: keywords) {
      if (keyword == null || keyword.isBlank()) continue;

      builder.and(Expressions.stringTemplate("replace({0}, ' ', '')", school.name).contains(keyword));
    }

    Long count = jpaQueryFactory
        .select(school.id.count())
        .from(school)
        .where(builder)
        .fetchOne();

    if (count == null || count == 0L) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(page, Math.max(size, 1)), 0);
    }

    size = (size <= 0) ? count.intValue() : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Sort sort = Sort.by(Order.asc("name"), Order.asc("campus"));
    List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(sort);

    List<Information> list = jpaQueryFactory
        .select(new QSchoolDto_Information(school.id, school.name, school.campus))
        .from(school)
        .where(builder)
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
        .offset((long) page*size)
        .limit(size)
        .fetch();

    return new PageImpl<>(list, PageRequest.of(page, size, sort), count);
  }

  @Override
  public Page<Information> searchCampusBySchool(Information standard, int page, int size) {
    //현재 들어온 id의 학교정보를 가장 위로 고정
    NumberExpression<Integer> rankPath = new CaseBuilder()
        .when(school.id.eq(standard.getId())).then(0).otherwise(1);

    Long count = jpaQueryFactory
        .select(school.id.count())
        .from(school)
        .where(school.name.startsWith(standard.getName()))
        .fetchOne();

    if (count == null || count == 0L) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(page, Math.max(size, 1)), 0);
    }

    size = (size <= 0) ? count.intValue() : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Sort sort = Sort.by(Order.asc("name"), Order.asc("campus"));
    List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(sort);
    orderSpecifiers.addFirst(rankPath.asc());

    List<Information> list = jpaQueryFactory
        .select(new QSchoolDto_Information(school.id, school.name, school.campus))
        .from(school)
        .where(school.name.startsWith(standard.getName()))
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
        .offset((long) page*size)
        .limit(size)
        .fetch();

    return new PageImpl<>(list, PageRequest.of(page, size, sort), count);
  }

  private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {
    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    for (Order order : sort) {
      OrderSpecifier<?> orderSpecifier = switch (order.getProperty()) {
        case "name" -> order.isAscending() ?
            school.name.asc() : school.name.desc();
        case "campus" -> order.isAscending() ?
            school.campus.asc() : school.campus.desc();
        case "schoolId" -> order.isAscending() ?
            school.id.asc() : school.id.desc();
        case "createdAt" -> order.isAscending() ?
            school.createdAt.asc() : school.createdAt.desc();
        case "updatedAt" -> order.isAscending() ?
            school.updatedAt.asc() : school.updatedAt.desc();
        default -> throw new IllegalArgumentException("Unknown sort property: " + order.getProperty());
      };
      orderSpecifiers.add(orderSpecifier);
    }
    return orderSpecifiers;
  }
}
