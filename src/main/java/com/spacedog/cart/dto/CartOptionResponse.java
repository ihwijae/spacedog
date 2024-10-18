package com.spacedog.cart.dto;

import lombok.Data;

@Data
public class CartOptionResponse {

    private Long id;
    private Long cartItemId;
    private String name;
    private int additionalPrice;
}
