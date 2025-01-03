package com.spacedog.category.component;

import com.spacedog.category.domain.CategoryItem;
import com.spacedog.category.dto.CategoryWithItemResponse;
import com.spacedog.category.repository.CategoryItemRepository;
import com.spacedog.category.repository.CategoryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryItemFinder {

    private final CategoryQueryRepository categoryQueryRepository;
    private final CategoryItemRepository categoryItemRepository;


    public List<CategoryWithItemResponse> findCategoryItems(Long categoryId, int pageNo, int pageSize) {

        return categoryQueryRepository.findCategoryItems(categoryId, pageNo, pageSize);
    }

    public List<CategoryItem> findCategoryItemsWithItem(Long itemId) {
        List<CategoryItem> categoryItems = categoryItemRepository.findByItemId(itemId);
        return categoryItems;
    }
}
