package com.spacedog.category.dto;

import com.spacedog.generic.Money;
import lombok.Builder;
import lombok.Data;

@Data
public class CategoryWithItemResponse {


    private Long id;
    private String name;

    private String description;
    private int price;
    private int stockQuantity;


    public CategoryWithItemResponse() {
    }

    @Builder
    public CategoryWithItemResponse(String name, String description, int price, int stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
