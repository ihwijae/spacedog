package com.spacedog.order.service;

import lombok.Data;

@Data
public class OrderItemResponse {

    private Long itemId;
    private String itemName;
    private String optionName;
    private int quantity;
    private int unitPrice;        // 단가 (상품 개당 가격)
    private int totalPrice; // 항목의 총 금액


}
