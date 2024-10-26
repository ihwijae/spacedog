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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.spacedog.category.domain.QCategory.category;
import static com.spacedog.category.domain.QCategoryItem.categoryItem;
import static com.spacedog.item.domain.QItem.item;
import static com.spacedog.item.domain.QItemOptionGroupSpecification.itemOptionGroupSpecification;
import static com.spacedog.item.domain.QItemOptionSpecification.itemOptionSpecification;
import static com.spacedog.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryAdapter implements ItemRepositoryPort {

    private final ItemRepository repository;
    private final JPAQueryFactory query;

    @Override
    public Optional<Item> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Item save(Item item) {
        return repository.save(item);
    }

    @Override
    public void delete(Long id) {
        Item item = repository.findById(id).orElseThrow();
        repository.delete(item);
    }

    @Override
    public void deleteAll(Iterable<Item> items) {
        repository.deleteAll(items);
    }

    @Override
    public void update(Long itemId, ItemEditRequest request) {
        Item findItem = repository.findById(itemId).orElseThrow();
        findItem.itemUpdate(request);
    }


    @Override
    public boolean existByName(String name) {
        Integer result = query
                .selectOne()
                .from(item)
                .where(item.name.eq(name))
                .fetchFirst();

        return result != null;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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
