package com.zerobase.babdeusilbun.repository.custom.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.babdeusilbun.domain.QCategory;
import com.zerobase.babdeusilbun.dto.CategoryDto.Information;
import com.zerobase.babdeusilbun.dto.QCategoryDto_Information;
import com.zerobase.babdeusilbun.repository.custom.CustomCategoryRepository;
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
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {
  private final JPAQueryFactory jpaQueryFactory;

  private final QCategory category = QCategory.category;

  @Override
  public Page<Information> getAllCategories(int page, int size) {
    Long count = jpaQueryFactory
        .select(category.id.count())
        .from(category)
        .fetchOne();

    if (count == null || count == 0L) {
      return new PageImpl<>(new ArrayList<>(), PageRequest.of(page, Math.max(size, 1)), 0);
    }

    size = (size <= 0) ? count.intValue() : size;
    page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

    Sort sort = Sort.by(Order.asc("name"));
    List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(sort);

    List<Information> list = jpaQueryFactory
        .select(new QCategoryDto_Information(category.id, category.name))
        .from(category)
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
            category.name.asc() : category.name.desc();
        case "categoryId" -> order.isAscending() ?
            category.id.asc() : category.id.desc();
        case "createdAt" -> order.isAscending() ?
            category.createdAt.asc() : category.createdAt.desc();
        case "updatedAt" -> order.isAscending() ?
            category.updatedAt.asc() : category.updatedAt.desc();
        default -> throw new IllegalArgumentException("Unknown sort property: " + order.getProperty());
      };
      orderSpecifiers.add(orderSpecifier);
    }
    return orderSpecifiers;
  }
}
