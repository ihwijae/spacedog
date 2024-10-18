package com.spacedog.cart.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartResponse {

    private List<ItemCartResponse> cartItems = new ArrayList<>();
    private int totalPrice;
    private int totalItems;


    @Builder
    public CartResponse(ItemCartResponse cartItems, int totalPrice, int totalItems) {
        this.cartItems.add(cartItems);
        this.totalPrice = totalPrice;
        this.totalItems = totalItems;
    }

    public CartResponse() {
    }



}
