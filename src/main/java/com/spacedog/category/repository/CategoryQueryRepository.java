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

import java.util.List;

import static com.spacedog.category.domain.QCategory.category;
import static com.spacedog.category.domain.QCategoryItem.categoryItem;
import static com.spacedog.item.domain.QItem.item;

@Repository
@RequiredArgsConstructor
public class CategoryQueryRepository {


    private final JPAQueryFactory queryFactory;


    public Page<CategoryWithItemResponse> findCategoryItems(Long categoryId, Pageable pageable) {

        List<CategoryWithItemResponse> CategoryWithItemResponse = queryFactory
                .select(Projections.fields(CategoryWithItemResponse.class,
                        item.id,
                        item.name,
                        item.description,
                        item.price,
                        item.stockQuantity
                ))
                .from(item)
                .join(item.category, categoryItem)
                .join(categoryItem.category, category)
                .where(categoryItem.category.id.eq(categoryId))
                .fetch();

        return new PageImpl<>(CategoryWithItemResponse, pageable, CategoryWithItemResponse.size());
    }
}
