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
public class EditOptionSpecsRequest {

    private Long id;
    private String name;

    private int price;
    private int quantity;

}
