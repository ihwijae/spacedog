package com.spacedog.item.dto;

import com.spacedog.generic.Money;
import com.spacedog.item.domain.ItemOptionGroupSpecification;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FindItemAllResponse {

    private Long id;
    private String name;

    private String description;
    private int price;
    private int stockQuantity;

    @Builder
    public FindItemAllResponse(String name, String description, int price, int stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public FindItemAllResponse() {
    }
}
