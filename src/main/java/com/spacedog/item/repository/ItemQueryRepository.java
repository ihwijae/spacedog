package com.spacedog.item.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spacedog.category.service.CategoryResponse;
import com.spacedog.item.domain.Item;
import com.spacedog.item.domain.QItem;
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


        public Optional<Item> findByItemWithCategory(Long id) {
        Item item = query
                .select(QItem.item)
                .from(QItem.item)
                .join(QItem.item.category, categoryItem).fetchJoin()
                .join(categoryItem.category, category).fetchJoin()
                .where(QItem.item.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(item);
    }

//    public List<ItemDetailResponse> findByItemDetail(Long itemId) {
//      List<ItemDetailResponse> findItemWithOption = query
//                .selectFrom(item)
//                .leftJoin(itemOptionGroupSpecification).on(item.id.eq(itemOptionGroupSpecification.item.id))
//                .leftJoin(itemOptionSpecification).on(itemOptionGroupSpecification.id.eq(itemOptionSpecification.optionGroupSpecification.id))
//                .join(item.category, categoryItem)
//                .join(categoryItem.category, category)
//                .where(item.id.eq(itemId))
//                .transform(
//                        groupBy(item.id).list(
//                                Projections.fields(ItemDetailResponse.class,
//                                        item.name,
//                                        item.description,
//                                        item.price,
//                                        item.stockQuantity,
//                                        category.as("category"),
//                                        list(
//                                                Projections.fields(OptionGroupResponse.class,
//                                                        itemOptionGroupSpecification.name,
//                                                        itemOptionGroupSpecification.exclusive,
//                                                        itemOptionGroupSpecification.basic,
//                                                        list(
//                                                                Projections.fields(OptionSpecsResponse.class,
//                                                                        itemOptionSpecification.name,
//                                                                        itemOptionSpecification.price
//                                                                )
//                                                        ))
//                                        )
//                                )
//                        )
//                );
//      return findItemWithOption;
//    }

    public List<ItemDetailResponse> itemDetail(Long itemId) {
        List<ItemDetailResponse> itemDetail = findItemDetail(itemId);
        itemDetail.forEach(
                itemDetailResponse -> {
                    List<OptionGroupResponse> optionGroup = findOptionGroup(itemId);
                    List<CategoryResponse> categoryResponses = findCategory(itemId);
                    itemDetailResponse.setCategory(categoryResponses);
                    itemDetailResponse.setOptionGroup(optionGroup);

                    optionGroup.forEach(optionGroupResponse -> {
                        List<OptionSpecsResponse> optionSpecs = findOptionSpecs();
                        optionGroupResponse.setOptionSpecs(optionSpecs);
                    });
                }
        );
        return itemDetail;
    }

    private List<ItemDetailResponse> findItemDetail(Long itemId) {
        return query
                .selectDistinct(Projections.fields(ItemDetailResponse.class,
                        item.name,
                        item.description,
                        item.price,
                        item.stockQuantity
                        ))
                .from(item)
                .where(item.id.eq(itemId))
                .fetch();
    }





    private List<CategoryResponse> findCategory(Long itemId) {
        return query
                .select(Projections.fields(CategoryResponse.class,
                        category.id,
                        category.name,
                        category.depth))
                .from(categoryItem) // categoryItem을 먼저 조인
                .join(categoryItem.category, category) // categoryItem과 category 조인
                .join(categoryItem.item, item) // categoryItem과 item 조인
                .where(item.id.eq(itemId)) // 조건 설정
                .fetch();
    }

    private List<OptionGroupResponse> findOptionGroup(Long itemId) {
        return query
                .select(Projections.fields(OptionGroupResponse.class,
                        itemOptionGroupSpecification.name,
                        itemOptionGroupSpecification.basic,
                        itemOptionGroupSpecification.exclusive))
                .from(itemOptionGroupSpecification)
                .join(item).on(itemOptionGroupSpecification.item.id.eq(item.id))
                .where(itemOptionGroupSpecification.item.id.eq(itemId))
                .fetch();
    }

    private List<OptionSpecsResponse> findOptionSpecs() {
        return query
                .select(Projections.fields(OptionSpecsResponse.class,
                        itemOptionSpecification.name,
                        itemOptionSpecification.price
                        ))
                .from(itemOptionSpecification)
                .join(itemOptionGroupSpecification).on(itemOptionSpecification.optionGroupSpecification.id.eq(itemOptionGroupSpecification.id))
                .fetch();
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
