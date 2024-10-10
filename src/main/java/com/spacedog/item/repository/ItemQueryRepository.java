package com.spacedog.item.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spacedog.item.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.spacedog.category.domain.QCategory.category;
import static com.spacedog.category.domain.QCategoryItem.categoryItem;
import static com.spacedog.item.domain.QItem.item;
import static com.spacedog.item.domain.QItemOptionGroupSpecification.itemOptionGroupSpecification;
import static com.spacedog.item.domain.QItemOptionSpecification.itemOptionSpecification;
import static com.spacedog.member.domain.QMember.member;
import static org.hibernate.Hibernate.list;

@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {

    private final JPAQueryFactory query;


    public List<SearchItemResponse> getItems(SearchItemRequest request) {
        return
                query
                        .select(Projections.fields(SearchItemResponse.class,
                                item.name,
                                item.description,
                                item.price,
                                item.stockQuantity,
                                member.name.as("memberName")))
                        .from(item)
                        .join(member).on(item.memberId.eq(member.id))
                        .where(
                                LikeItemName(request.getSearchName()),
                                LikeItemContent(request.getSearchContent())
                        )
                        .fetch();
    }


//        public Optional<Item> findByItemWithCategory(Long id) {
//        Item item = query
//                .select(QItem.item)
//                .from(QItem.item)
//                .join(QItem.item.category, categoryItem).fetchJoin()
//                .join(categoryItem.category, category).fetchJoin()
//                .where(QItem.item.id.eq(id))
//                .fetchOne();
//        return Optional.ofNullable(item);
//    }

    public ItemDetailResponse findByItemDetail(Long itemId) {
                query
                        .selectFrom(item)
                        .leftJoin(itemOptionGroupSpecification).on(item.id.eq(itemOptionGroupSpecification.item.id))
                        .leftJoin(itemOptionSpecification).on(itemOptionGroupSpecification.id.eq(itemOptionSpecification.optionGroupSpecification.id))
                        .join(item.category, categoryItem)
                        .join(categoryItem.category, category)
                        .where(item.id.eq(itemId))
                        .transform(
                                groupBy(item.id).list(
                                        Projections.fields(ItemDetailResponse.class,
                                                item.name,
                                                item.description,
                                                item.price,
                                                item.stockQuantity,
                                                category.name.as("category"),
                                                list( // alias
                                                        Projections.fields(OptionGroupResponse.class,
                                                                itemOptionGroupSpecification.name,
                                                                itemOptionGroupSpecification.exclusive,
                                                                itemOptionGroupSpecification.basic,
                                                                list( // OptionSpecsResponse 리스트
                                                                        Projections.fields(OptionSpecsResponse.class,
                                                                                itemOptionSpecification.name.as("name"),
                                                                                itemOptionSpecification.price.as("price")
                                                                        )
                                                                ).as("optionSpecs")
                                                        )
                                                ).as("optionGroup")
                                        )
                                ))
    }




    private BooleanExpression LikeItemName(String searchName) {
        if(StringUtils.hasText(searchName)) {
            return item.name.like("%" + searchName + "%");
        }
        return null;
    }

    private BooleanExpression LikeItemContent(String searchContent) {
        if(StringUtils.hasText(searchContent)) {
            return item.description.like("%" + searchContent + "%");
        }
        return null;
    }




}
