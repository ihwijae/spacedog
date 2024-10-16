package com.spacedog.item.dto;

import com.spacedog.category.domain.Category;
import com.spacedog.category.service.CategoryResponse;
import com.spacedog.generic.Money;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDetailResponse {

    private Long id;

    private String name;

    private String description;

    private int price;

    private int stockQuantity;

    private List<CategoryResponse> category;

    private List<OptionGroupResponse> optionGroup;

    @Builder
    public ItemDetailResponse(String name, String description, int price, int stockQuantity, List<CategoryResponse> category, List<OptionGroupResponse> optionGroup) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.optionGroup = optionGroup;
    }

    public ItemDetailResponse() {
    }
}
