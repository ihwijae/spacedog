package com.spacedog.category.service;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CategoryResponse {


    private Long id;

    private String name;
    private Long depth;

    @Builder
    public CategoryResponse(Long id, String name, Long depth) {
        this.id = id;
        this.name = name;
        this.depth = depth;
    }

    public CategoryResponse() {
    }


}
