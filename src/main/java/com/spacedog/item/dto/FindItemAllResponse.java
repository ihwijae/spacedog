package com.spacedog.item.dto;

import lombok.Builder;
import lombok.Data;

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
