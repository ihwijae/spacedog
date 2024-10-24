package com.spacedog.category.service;

import com.spacedog.category.domain.Category;
import com.spacedog.category.domain.CategoryItem;
import com.spacedog.category.dto.CategoryDto;
import com.spacedog.category.dto.CategoryWithItemResponse;
import com.spacedog.category.exception.CategoryNotFoundException;
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

    private final CategoryRepository categoryRepository;
    private final CategoryQueryRepository categoryQueryRepository;
    private final CategoryItemRepository categoryItemRepository;


    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoryList() {

        List<Category> mainCategory = categoryRepository.findByParentIsNull();

        if(mainCategory.isEmpty()) {
            throw new CategoryNotFoundException("메인 카테고리가 존재하지 않습니다");
        }

        return mainCategory.stream()
                .map(m -> CategoryMapper.INSTANCE.toDto(m))
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<CategoryWithItemResponse> findCategoryItems (Long categoryId, int pageNo, int pageSize) {

        return categoryQueryRepository.findCategoryItems(categoryId, pageNo, pageSize);
    }

    @Transactional
    public void saveCategoryItem(CreateItemRequest createItemRequest, Item item) {
        List<Long> categoryIds = createItemRequest.getCategoryIds();

        categoryIds.forEach(c -> {
            Category findCategory = categoryRepository.findById(c)
                    .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다"));

            CategoryItem categoryItem = CategoryItem.createCategoryItem(findCategory, item);
            categoryItemRepository.save(categoryItem);

        });

    }
}
