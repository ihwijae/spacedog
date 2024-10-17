package com.spacedog.cart.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartResponse {

    private Long itemId;
    private List<ItemCartResponse> cartItems = new ArrayList<>();
    private int totalPrice;
    private int totalAmount;


}
