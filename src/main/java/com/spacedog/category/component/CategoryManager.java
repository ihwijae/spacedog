package com.spacedog.category.component;

import com.spacedog.category.domain.CategoryItem;
import com.spacedog.category.repository.CategoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryManager {

    private final CategoryItemRepository categoryItemRepository;

    public void save (CategoryItem categoryItem) {

        categoryItemRepository.save(categoryItem);

    }

    public void deleteAll(List<CategoryItem> categoryItems) {

        categoryItemRepository.deleteAllInBatch(categoryItems);
    }
}
