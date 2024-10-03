package com.spacedog.category.controller;

import com.spacedog.category.service.CategoryDto;
import com.spacedog.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    @Cacheable(cacheNames = "categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> categoryList = categoryService.getCategoryList();

        return ResponseEntity.ok(categoryList);
    }
}
