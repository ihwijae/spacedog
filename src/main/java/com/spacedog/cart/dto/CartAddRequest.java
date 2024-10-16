package com.spacedog.cart.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartAddRequest {

    private Long itemId;

    private Long cartId;

    private int quantity;

    private List<Long> optionSpecsIds = new ArrayList<>();

    @Builder
    public CartAddRequest(Long itemId, Long cartId, int quantity, List<Long> optionSpecsIds) {
        this.itemId = itemId;
        this.cartId = cartId;
        this.quantity = quantity;
        this.optionSpecsIds = optionSpecsIds;
    }

    public CartAddRequest() {
    }
}
