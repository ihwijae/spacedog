package com.spacedog.item.dto;

import com.spacedog.generic.Money;

import com.spacedog.generic.MoneyConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OptionSpecsResponse {

    private String name;
    private Money price;


    public OptionSpecsResponse(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    public OptionSpecsResponse() {
    }
}
