package com.spacedog.item.dto;

import com.spacedog.generic.Money;
import com.spacedog.item.domain.ItemOptionGroupSpecification;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FindItemAllResponse {

    private String name;

    private String description;
    private Money price;
    private int stockQuantity;
    private Long categoryId;

    private List<ItemOptionGroupSpecification> itemOption = new ArrayList<>();

}
