package com.spacedog.category.service;

import com.spacedog.category.domain.Category;
import com.spacedog.category.dto.CategoryDto;
import com.spacedog.category.dto.CategoryWithItemResponse;
import com.spacedog.category.exception.CategoryNotFoundException;
import com.spacedog.category.repository.CategoryQueryRepository;
import com.spacedog.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
}
