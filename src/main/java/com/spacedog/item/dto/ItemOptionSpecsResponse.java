package com.spacedog.item.dto;

import com.spacedog.generic.Money;
import lombok.Data;

@Data
public class ItemOptionSpecsResponse {

    private String name;
    private Money price;
}
