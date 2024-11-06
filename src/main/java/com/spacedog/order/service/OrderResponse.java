package com.spacedog.order.service;

import com.spacedog.order.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {


    private String orderNumber;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private int totalAmount;
    private List<OrderItemResponse> orderItems;

}
