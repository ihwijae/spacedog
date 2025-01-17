package com.spacedog.category.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spacedog.category.domain.QCategory;
import com.spacedog.category.domain.QCategoryItem;
import com.spacedog.category.dto.CategoryWithItemResponse;
import com.spacedog.item.domain.QItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.spacedog.category.domain.QCategory.category;
import static com.spacedog.category.domain.QCategoryItem.categoryItem;
import static com.spacedog.item.domain.QItem.item;

@Repository
@RequiredArgsConstructor
public class CategoryQueryRepository {


    private final JPAQueryFactory queryFactory;


    public List<CategoryWithItemResponse> findCategoryItems(Long categoryId, int pageNo, int pageSize) {

//        List<CategoryWithItemResponse> CategoryWithItemResponse = queryFactory
//                .select(Projections.fields(CategoryWithItemResponse.class,
//                        item.id,
//                        item.name,
//                        item.description,
//                        item.price,
//                        item.stockQuantity
//                ))
//                .from(item)
//                .join(item.category, categoryItem)
//                .join(categoryItem.category, category)
//                .where(categoryItem.category.id.eq(categoryId))
//                .fetch();
//
//        return new PageImpl<>(CategoryWithItemResponse, pageable, CategoryWithItemResponse.size());

        /**
         * 커버링 인덱스 사용
         */
        List<Long> ids = queryFactory
                .select(item.id)
                .from(item)
                .join(item.category, categoryItem)
                .join(categoryItem.category, category)
                .where(categoryItem.category.id.eq(categoryId))
                .orderBy(categoryItem.id.desc())
                .limit(pageSize)
                .offset(pageNo * pageSize)
                .fetch();

        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        return queryFactory
                .select(Projections.fields(CategoryWithItemResponse.class,
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
}
