package com.spacedog.order.service;

import lombok.Data;

@Data
public class OrderItemResponse {

    private Long itemId;
    private String itemName;
    private int quantity;
    private int price;
}
