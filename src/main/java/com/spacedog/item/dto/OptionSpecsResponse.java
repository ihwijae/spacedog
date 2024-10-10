package com.spacedog.item.dto;

import com.spacedog.generic.Money;

import com.spacedog.generic.MoneyConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionSpecsResponse {

    private String name;
    private Money price;




}
