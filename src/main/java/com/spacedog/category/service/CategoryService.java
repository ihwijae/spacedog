package com.spacedog.category.service;

import com.spacedog.category.component.CategoryFinder;
import com.spacedog.category.component.CategoryItemFinder;
import com.spacedog.category.component.CategoryManager;
import com.spacedog.category.domain.Category;
import com.spacedog.category.domain.CategoryItem;
import com.spacedog.category.dto.CategoryDto;
import com.spacedog.category.dto.CategoryWithItemResponse;
import com.spacedog.category.exception.CategoryNotFoundException;
import com.spacedog.category.exception.CategoryNotFoundException.CategoryOfNot;
import com.spacedog.category.repository.CategoryItemRepository;
import com.spacedog.category.repository.CategoryQueryRepository;
import com.spacedog.category.repository.CategoryRepository;
import com.spacedog.item.domain.Item;
import com.spacedog.item.dto.CreateItemRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryFinder categoryFinder;
    private final CategoryManager categoryManager;
    private final CategoryItemFinder categoryItemFinder;


    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoryList() {

        List<Category> mainCategory = categoryFinder.findMainCategory();

        if(mainCategory.isEmpty()) {
            throw new CategoryNotFoundException("메인 카테고리가 존재하지 않습니다");
        }

        return mainCategory.stream()
                .map(m -> CategoryMapper.INSTANCE.toDto(m))
                .collect(Collectors.toList());

    }


    @Transactional(readOnly = true)
    public List<CategoryWithItemResponse> findCategoryItems (Long categoryId, int pageNo, int pageSize) {

        return categoryItemFinder.findCategoryItems(categoryId, pageNo, pageSize);
    }


    public void saveCategoryItem(CreateItemRequest createItemRequest, Item item) {
        List<Long> categoryIds = createItemRequest.getCategoryIds();

        if(categoryIds.isEmpty()) {
            throw new CategoryOfNot("카테고리가 없으면 등록할 수 없습니다");
        }

                categoryIds.forEach(c -> {
            Category findCategory = categoryFinder.findCategory(c);

                    CategoryItem categoryItem = CategoryItem.createCategoryItem(findCategory, item);
                    categoryManager.save(categoryItem);
        });

    }

    @Transactional
    public void deleteCategoryItem(Long itemId) {

        List<CategoryItem> findCategoryItems = categoryItemFinder.findCategoryItemsWithItem(itemId);
        categoryManager.deleteAll(findCategoryItems);
    }
}
