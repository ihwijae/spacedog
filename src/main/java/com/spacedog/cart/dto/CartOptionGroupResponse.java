package com.spacedog.cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartOptionGroupResponse {

    private String name;
    private List<CartOptionResponse> options;

}
