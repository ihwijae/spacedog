package com.spacedog.category.component;

import com.spacedog.category.domain.Category;
import com.spacedog.category.exception.CategoryNotFoundException;
import com.spacedog.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryFinder {

    private final CategoryRepository categoryRepository;


    public Category findCategory(Long categoryId) {
       return categoryRepository.findById(categoryId)
                .orElseThrow( () -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다"));
    }

    public List<Category> findMainCategory() {
        return categoryRepository.findByParentIsNull();
    }


}
