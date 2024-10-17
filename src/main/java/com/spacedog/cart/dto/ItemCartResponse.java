package com.spacedog.cart.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemCartResponse {

        private Long itemId;
        private String itemName;
        private int totalPrice; //각 상품의 가격 (기본가격 + 옵션별 추가 금액)
        private int quantity;
        private List<CartOptionResponse> options = new ArrayList<>();
}
