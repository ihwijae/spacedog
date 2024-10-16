package com.spacedog.item.dto;

import com.spacedog.generic.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionSpecsRequest {

    private String name;

    private int price;
}
