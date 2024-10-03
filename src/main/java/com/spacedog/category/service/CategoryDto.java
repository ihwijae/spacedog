package com.spacedog.category.service;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryDto {

    private Long id;

    private String name;
    private Long depth;
    private List<CategoryDto> children;

}
