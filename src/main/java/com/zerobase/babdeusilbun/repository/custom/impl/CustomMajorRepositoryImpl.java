package com.zerobase.babdeusilbun.repository.custom.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.babdeusilbun.domain.QMajor;
import com.zerobase.babdeusilbun.dto.QMajorDto_Information;
import com.zerobase.babdeusilbun.dto.MajorDto.Information;
import com.zerobase.babdeusilbun.repository.custom.CustomMajorRepository;
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
public class CustomMajorRepositoryImpl implements CustomMajorRepository {
    private final JPAQueryFactory jpaQueryFactory;

    private final QMajor major = QMajor.major;

    @Override
    public Page<Information> searchMajorNameByKeywords(String[] keywords, int page, int size) {
        BooleanBuilder builder = new BooleanBuilder();

        for (String keyword: keywords) {
            if (keyword == null || keyword.isBlank()) continue;

            builder.and(Expressions.stringTemplate("replace({0}, ' ', '')", major.name).contains(keyword));
        }

        Long count = jpaQueryFactory
                .select(major.id.count())
                .from(major)
                .where(builder)
                .fetchOne();

        if (count == null || count == 0L) {
            return new PageImpl<>(new ArrayList<>(), PageRequest.of(page, Math.max(size, 1)), 0);
        }

        size = (size <= 0) ? count.intValue() : size;
        page = Math.min(page, ((int) Math.ceil((double) count / size))-1);

        Sort sort = Sort.by(Order.asc("name"));
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(sort);

        List<Information> list = jpaQueryFactory
                .select(new QMajorDto_Information(major.id, major.name))
                .from(major)
                .where(builder)
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
                        major.name.asc() : major.name.desc();
                case "majorId" -> order.isAscending() ?
                        major.id.asc() : major.id.desc();
                case "createdAt" -> order.isAscending() ?
                        major.createdAt.asc() : major.createdAt.desc();
                case "updatedAt" -> order.isAscending() ?
                        major.updatedAt.asc() : major.updatedAt.desc();
                default -> throw new IllegalArgumentException("Unknown sort property: " + order.getProperty());
            };
            orderSpecifiers.add(orderSpecifier);
        }
        return orderSpecifiers;
    }
}
