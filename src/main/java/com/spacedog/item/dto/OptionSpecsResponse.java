package com.spacedog.item.dto;

import com.spacedog.generic.Money;

import com.spacedog.generic.MoneyConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OptionSpecsResponse {

    private Long optionGroupId;
    private String name;
    private int price;


    public OptionSpecsResponse(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public OptionSpecsResponse() {
    }
}
