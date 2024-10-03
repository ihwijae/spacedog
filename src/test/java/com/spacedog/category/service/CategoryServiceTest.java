package com.spacedog.category.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {


    @Autowired
    private CategoryService categoryService;


    @Test
    @DisplayName("모든 카테고리 가져오기")
    public void categoryAllList() {

        //given
        categoryService.getCategoryList();

        //then

        //then
    }

}