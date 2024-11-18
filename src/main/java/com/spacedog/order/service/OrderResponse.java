package com.spacedog.order.service;

import com.spacedog.order.domain.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = "orderId")
public class OrderResponse {


    private Long orderId;
    private String orderNumber;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItemResponse> orderItems;

}
