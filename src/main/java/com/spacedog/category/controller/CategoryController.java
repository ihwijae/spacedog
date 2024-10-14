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
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<List<CategoryWithItemResponse>> findCategoryItems(@PathVariable Long categoryId,
                                                                         @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        List<CategoryWithItemResponse> categoryItems = categoryService.findCategoryItems(categoryId, pageNo, pageSize);

        return ApiResponse.success(categoryItems, "카테고리별 아이템 조회");
    }

}
