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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
                                member.name.as("memberName")))
                        .from(item)
                        .join(member).on(item.memberId.eq(member.id))
                        .where(
                                LikeItemName(request.getSearchName()),
                                LikeItemContent(request.getSearchContent())
                        )
                        .fetch();
    }

    public List<FindItemAllResponse> findItemsAll(int pageNo, int pageSize) {
        List<Long> ids = query
                .select(item.id)
                .from(item)
                .orderBy(item.id.desc())
                .limit(pageSize)
                .offset(pageNo * pageSize)
                .fetch();

        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        return query
                .select(Projections.fields(FindItemAllResponse.class,
                        item.id,
                        item.name,
                        item.description,
                        item.price
                        ))
                .from(item)
                .where(item.id.in(ids))
                .orderBy(item.id.desc())
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

    /** 추후 리팩터링 예정
     * 여러개의 상품 조회시 N+1 문제 최적화 코드
     * **/

//    public List<ItemDetailResponse> findByItemDetail(Long itemId) {
//
//        //루트 조회
//        List<ItemDetailResponse> itemDetail = findItemDetail(itemId);
//
//        // optionGroup, optionSpecs 컬렉션을 Map 으로 한방에 조회
//        Map<Long, List<OptionGroupResponse>> optionGroupMap = findOptionGroupMap(toOptionGroupIds(itemDetail));
//        Map<Long, List<OptionSpecsResponse>> optionSpecsMap = findOptionSpecsMap(toOptionSpecsIds(itemDetail));
//
//        //루프를 돌면서 컬렉션 추가 (추가 쿼리 실행 xx)
//        itemDetail.forEach(
//                itemDetailResponse -> {
//                    itemDetailResponse.setOptionGroup(optionGroupMap.get(itemDetailResponse.getId()));
//
//                }
//        );
//
//    }
//
//    private Map<Long, List<OptionGroupResponse>> findOptionGroupMap(List<Long> itemIds) {
//        List<OptionGroupResponse> optionGroupResponses = query
//                .select(Projections.fields(OptionGroupResponse.class,
//                        itemOptionGroupSpecification.name,
//                        itemOptionGroupSpecification.exclusive,
//                        itemOptionGroupSpecification.basic))
//                .from(itemOptionGroupSpecification)
//                .join(item).on(itemOptionGroupSpecification.item.id.eq(item.id))
//                .where(itemOptionGroupSpecification.item.id.in(itemIds))
//                .fetch();
//
//        Map<Long, List<OptionGroupResponse>> optionGroupMap = optionGroupResponses.stream()
//                .collect(Collectors.groupingBy(optionGroup -> optionGroup.getId()));
//
//        return optionGroupMap;
//    }
//
//    private Map<Long, List<OptionSpecsResponse>> findOptionSpecsMap(List<Long> itemIds) {
//        List<OptionSpecsResponse> optionSpecsResponses = query
//                .select(Projections.fields(OptionSpecsResponse.class,
//                        itemOptionSpecification.name,
//                        itemOptionSpecification.price))
//                .from(itemOptionSpecification)
//                .join(itemOptionGroupSpecification).on(itemOptionSpecification.optionGroupSpecification.id.eq(itemOptionGroupSpecification.id))
//                .fetch();
//
//        Map<Long, List<OptionSpecsResponse>> result = optionSpecsResponses.stream()
//                .collect(Collectors.groupingBy(optionSpecs -> optionSpecs.getId()));
//
//        return result;
//    }
//
//    private List<Long> toOptionGroupIds(List<ItemDetailResponse> result) {
//        List<Long> optionGroupIds = result.stream()
//                .map(o -> o.getId())
//                .collect(Collectors.toList());
//
//        return optionGroupIds;
//    }
//
//    private List<Long> toOptionSpecsIds(List<ItemDetailResponse> result) {
//        List<Long> optionSpecsIds = result.stream()
//                .map(o -> o.getId())
//                .collect(Collectors.toList());
//
//        return optionSpecsIds;
//    }


    /**
     * 여러개의 상품의 조회할때 ( ex)전체 아이템조회, 카테고리별 아이템 조회 등등..) 단일 조회시에는 해당 x
     * N+1 문제 발생 조회하는 아이템의 갯수마다 N+1 쿼리가 실행된다 .
     * 조회하는 아이템이 10개라면 컬렉션을 조회하는 10번의 쿼리가 추가로 발생한다.
     * **/


    public List<ItemDetailResponse> itemDetail(Long itemId) {
        List<ItemDetailResponse> itemDetail = findItemDetail(itemId);
        itemDetail.forEach(
                itemDetailResponse -> {
                    List<OptionGroupResponse> optionGroup = findOptionGroup(itemId);
                    List<CategoryResponse> categoryResponses = findCategory(itemId);
                    itemDetailResponse.setCategory(categoryResponses);
                    itemDetailResponse.setOptionGroup(optionGroup);

                    optionGroup.forEach(optionGroupResponse -> {
                        List<OptionSpecsResponse> optionSpecs = findOptionSpecs(optionGroupResponse.getId());
                        optionGroupResponse.setOptionSpecs(optionSpecs);
                    });
                }
        );
        return itemDetail;
    }

    private List<ItemDetailResponse> findItemDetail(Long itemId) {
        return query
                .selectDistinct(Projections.fields(ItemDetailResponse.class,
                        item.id,
                        item.name,
                        item.description,
                        item.price
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
                .where(categoryItem.item.id.eq(itemId)) // 조건 설정
                .fetch();
    }

    private List<OptionGroupResponse> findOptionGroup(Long itemId) {
        return query
                .selectDistinct(Projections.fields(OptionGroupResponse.class,
                        itemOptionGroupSpecification.id,
                        itemOptionGroupSpecification.name,
                        itemOptionGroupSpecification.basic,
                        itemOptionGroupSpecification.exclusive))
                .from(itemOptionGroupSpecification)
                .join(item).on(itemOptionGroupSpecification.item.id.eq(item.id))
                .where(itemOptionGroupSpecification.item.id.eq(itemId))
                .fetch();
    }

    private List<OptionSpecsResponse> findOptionSpecs (Long optionGroupId) {
        return query
                .selectDistinct(Projections.fields(OptionSpecsResponse.class,
                        itemOptionSpecification.id,
                        itemOptionSpecification.name,
                        itemOptionSpecification.additionalPrice
                        ))
                .from(itemOptionSpecification)
                .join(itemOptionGroupSpecification).on(itemOptionSpecification.optionGroupSpecification.id.eq(itemOptionGroupSpecification.id))
                .where(itemOptionGroupSpecification.id.eq(optionGroupId))
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
