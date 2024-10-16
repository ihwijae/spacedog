package com.spacedog.item.dto;

import com.spacedog.generic.Money;
import lombok.Data;

@Data
public class SearchItemResponse {


    private String name;
    private String description;
    private String memberName;
    private int stockQuantity;
    private int price;
    private Long categoryId;

}
