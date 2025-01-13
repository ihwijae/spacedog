package com.spacedog.option.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spacedog.option.domain.QOptionGroupSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spacedog.option.domain.QOptionGroupSpecification.optionGroupSpecification;

@Repository
@RequiredArgsConstructor
public class OptionQueryRepository {

    private final JPAQueryFactory queryFactory;


    public List<Long> findOptionGroupIds(Long itemId) {

        return queryFactory
                .select(optionGroupSpecification.id)
                .from(optionGroupSpecification)
                .where(optionGroupSpecification.item.id.eq(itemId))
                .fetch();
    }



}
