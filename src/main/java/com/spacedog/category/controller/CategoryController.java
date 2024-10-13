package com.spacedog.category.controller;

import com.spacedog.category.dto.CategoryDto;
import com.spacedog.category.dto.CategoryWithItemResponse;
import com.spacedog.category.service.CategoryService;
import com.spacedog.global.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    @Cacheable(cacheNames = "categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> categoryList = categoryService.getCategoryList();

        return ResponseEntity.ok(categoryList);
    }

    @GetMapping("/categoryItems/{categoryId}")
    public ApiResponse<Page<CategoryWithItemResponse>> findCategoryItems(@PathVariable Long categoryId, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable ) {
        Page<CategoryWithItemResponse> categoryItems = categoryService.findCategoryItems(categoryId, pageable);

        return ApiResponse.success(categoryItems, "조회 했습니다");
    }

}
