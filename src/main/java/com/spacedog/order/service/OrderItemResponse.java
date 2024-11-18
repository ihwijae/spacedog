package com.spacedog.order.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrderItemResponse {

    @JsonIgnore
    private Long orderId;
    private Long itemId;
    private String itemName;
    private String optionName;
    private int quantity;
    private int price; // 항목의 총 금액

}
